package edu.uclm.esi.tysweb2023.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.tysweb2023.http.UserController;
import edu.uclm.esi.tysweb2023.model.Tablero;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import jakarta.servlet.http.HttpSession;

@Component
public class WSTablero extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private ConcurrentHashMap<String, SesionWS> sessionsByNombre = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, SesionWS> sessionsById = new ConcurrentHashMap<>();
	
	public String obtenerHttpId(WebSocketSession session) {
		HttpHeaders headers = session.getHandshakeHeaders();
		Collection<List<String>> values = headers.values();
		String httpSessionId = null;
		for (List<String> value : values) {
			for (String cadena : value) {
				if (cadena.startsWith("JSESSIONID")) {
					httpSessionId = cadena.substring(11);
					break;
				}
			}
			if (httpSessionId!=null)
				break;
		}
		return httpSessionId;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{				
		String httpSessionId = obtenerHttpId(session);
		ManagerWS.get().setWebsocketSession(httpSessionId, session);
	}
	
		
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload());
		String type = jso.getString("type");
		
		if (type.equals("MOVEMENT")) {
			String matchId = jso.getString("matchId");
			int columna = jso.getInt("col");
			
			//Sacar de la session el nombre del jugador
			String httpSessionId = obtenerHttpId(session);
			HttpSession hs =  UserController.httpSessions.get(httpSessionId);
			User user = (User) hs.getAttribute("user");
			
			//Match update
			MatchService ms =  ManagerWS.get().getMatchService();
			
			String personaActualizarTablero = obtenerSiguienteNombre(ms.findMatch(matchId).getPlayers(),user.getNombre());
			Tablero tb = ms.findMatch(matchId);
			
			JSONArray casillas = tb.mostrarCasillas();
			jso = new JSONObject();
			jso.put("type", "MATCH UPDATE");
			jso.put("player", personaActualizarTablero);
			jso.put("matchId", matchId);
			jso.put("board", casillas);
			
			if (ms.findMatch(matchId).getGanador() != Character.MIN_VALUE) {
				char ganador = ms.findMatch(matchId).getGanador();
				jso.put("winner", ganador);
				List<User> jugadoresPartida = tb.getPlayers();
				String nick_ganador="";
				
				if (ganador == 'R') {
					nick_ganador = jugadoresPartida.get(0).getNombre();
				}else {
					nick_ganador = jugadoresPartida.get(1).getNombre();
				}
				jso.put("nickWinner", nick_ganador);
			}
			ms.notificarMovimiento(matchId, jso);
		}
	}
	
	
	public static void send(WebSocketSession sessionWs, Object... clavesyValores) {
		JSONObject objJson = new JSONObject();
		//i+=2 Clave/valor
	    for (int i = 0; i < clavesyValores.length; i += 2) {
	    	objJson.put(clavesyValores[i].toString(), clavesyValores[i + 1]);
	    }
	    
	    WebSocketMessage<?> webSocketMessage = new TextMessage(objJson.toString());
	    
	    try {
	    	sessionWs.sendMessage(webSocketMessage);
	    } catch (IOException e) {
	        System.out.println("[Método SEND] Se ha producido una excepción:" +e.getMessage());
	    }
	}


	private void eliminarSesion(WebSocketSession session) {
		this.sessions.remove(session);
		SesionWS sesionWS = this.sessionsById.remove(session.getId());
		this.sessionsByNombre.remove(sesionWS.getNombre());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//SesionWS sesionWS = this.sessionsById.remove(session.getId());
		//this.sessionsByNombre.remove(sesionWS.getNombre());
		SesionWS hwSession = ManagerWS.get().getSessionByWebSocketId(session.getId());
		//hwSession.setWebsocketSession(null);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
	
	private static String obtenerSiguienteNombre(List<User> list, String nombreBuscado) {
		String nombre = "";
        if (list.get(0).getNombre().equals(nombreBuscado)){
        	nombre = list.get(1).getNombre();
        }else {
        	nombre = list.get(0).getNombre();
        }
        return nombre;
    }
}