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
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.tysweb2023.http.UserController;
import edu.uclm.esi.tysweb2023.model.User;
import jakarta.servlet.http.HttpSession;

@Component
public class WSTablero extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private ConcurrentHashMap<String, SesionWS> sessionsByNombre = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, SesionWS> sessionsById = new ConcurrentHashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		
		String query = session.getUri().getQuery();
		String httpSessionId = query.substring("httpId=".length());
		
		
		ManagerWS.get().setWebsocketSession(httpSessionId, session);
		
		/**
		ConcurrentHashMap<String, Object> datosProcesados = extraerCadenas(datos);
		String httpId = (String) datosProcesados.get("httpId");
		
		//Establecemos la session websocket en el usuario
		HttpSession httpSession = UserController.httpSessions.get(httpId);
		
		SesionWS sesionWs = new SesionWS(session);
		
		User user = (User) httpSession.getAttribute("user");
		sesionWs.setNombre(user.getNombre());
		user.setSesionWS(sesionWs);
		
		this.sessionsById.put(session.getId(), sesionWs);
		this.sessions.add(session); **/
	}
	
	private ConcurrentHashMap<String, Object> extraerCadenas(String cadena){
		cadena = cadena.replace("?", "&");

		String[] parametros = cadena.split("&");

		String httpId = "";
		String idPartida = "";

		for (String parametro : parametros) {
		    if (parametro.startsWith("httpId=")) {
		        httpId = parametro.split("=")[1];
		    } else if (parametro.startsWith("idPartida=")) {
		        idPartida = parametro.split("=")[1];
		    }
		}
				
		ConcurrentHashMap<String, Object> cadenaObtenida = new ConcurrentHashMap<>();
		cadenaObtenida.put("httpId", httpId);
		cadenaObtenida.put("idPartida", idPartida);
		
		return cadenaObtenida;
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
	}
	
	private void bienvenida(WebSocketSession sessionDelTipoQueAcabaDeLlegar) {
		
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

	private void difundir(WebSocketSession remitente, Object... clavesyValores) {
		
	}

	private void eliminarSesion(WebSocketSession session) {
		this.sessions.remove(session);
		SesionWS sesionWS = this.sessionsById.remove(session.getId());
		this.sessionsByNombre.remove(sesionWS.getNombre());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		SesionWS sesionWS = this.sessionsById.remove(session.getId());
		this.sessionsByNombre.remove(sesionWS.getNombre());
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
}