package edu.uclm.esi.tysweb2023.ws;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class WSGames extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private Map<String, SesionWS> sessionByNombre = new HashMap<>();
	private Map<String, SesionWS> sessionById = new HashMap<>();
	
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
			SesionWS sesionWS = new SesionWS(nombre, session);
			
			this.sessionByNombre.put(nombre, sesionWS);
			this.sessionById.put(session.getId(), sesionWS);
			return;
		}
		
		//{tipo: "IDENT", nombre: "MACARIO", texto: "Hola"} ejemplo de respuesta json
		
		if (tipo.equals("MENSAJE PRIVADO")) {
			String destinatario = jso.getString("destinatario");
			String texto = jso.getString("texto");
			
			//Buscamos al remitente
			String remitente = this.sessionById.get(session.getId()).getNombre();
			
			JSONObject respuesta = new JSONObject()
					.put("tipo", "MENSAJE PRIVADO")
					.put("texto", texto)
					.put("remitente", remitente);
			
			SesionWS sesionDestinatario = this.sessionByNombre.get(destinatario);
			if (sesionDestinatario == null) {
				respuesta.put("tipo", "SE FUE");
				TextMessage messageRespuesta = new TextMessage(respuesta.toString());
			}else {
				WebSocketSession sessionDestinatario = this.sessionByNombre.get(destinatario).getSession();
				TextMessage messageRespuesta = new TextMessage(respuesta.toString());
				sessionDestinatario.sendMessage(messageRespuesta);
			}

			return;
		}
		
		/*for(WebSocketSession s: this.sessions)
			s.sendMessage(message);*/
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		SesionWS sesionWS = this.sessionById.remove(session.getId());
		this.sessionByNombre.remove(sesionWS.getNombre());
		
	}
	
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {
	}
}

