package edu.uclm.esi.tysweb2023.ws;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class WSGames extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private Map<String, WebSocketSession> sessionByNombre = new HashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.sessions.add(session);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		JSONObject jso = new JSONObject(message.getPayload());
		String tipo = jso.getString("tipo");
		if (tipo.equals("IDENT")) {
			String nombre = jso.getString("nombre");
			this.sessionByNombre.put(nombre, session);
		}
		
		/*for(WebSocketSession s: this.sessions)
			s.sendMessage(message);*/
	}
	
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {
	}
}

