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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.tysweb2023.services.MatchService;

@Component
public class WSTablero extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private ConcurrentHashMap<String, SesionWS> sessionsByNombre = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, SesionWS> sessionsById = new ConcurrentHashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{
		
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

		ManagerWS.get().setWebsocketSession(httpSessionId, session);
		
		//prueba
		/*JSONObject jso = new JSONObject();
		jso.put("message", "prueba desde el servidor");
		WebSocketMessage<?> message = new TextMessage(jso.toString());
		session.sendMessage(message);*/

	}
	
		
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload());
		String type = jso.getString("type");
		
		if (type.equals("MOVEMENT")) {
			String matchId = jso.getString("matchId");
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
		hwSession.setWebsocketSession(null);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
}