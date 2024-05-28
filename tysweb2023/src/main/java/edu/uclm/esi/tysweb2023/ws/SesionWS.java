package edu.uclm.esi.tysweb2023.ws;

import org.springframework.web.socket.WebSocketSession;

import jakarta.servlet.http.HttpSession;

/**
 * Clase que actúa como wrapper
 * entre la sesión de websocket y
 * la httpSession
 */
public class SesionWS {
	
	private String nombre;
	private HttpSession httpSession;
	private WebSocketSession websocketSession;
	
	public SesionWS(WebSocketSession wSession, HttpSession httpSession) {
		this.websocketSession = wSession;
		this.httpSession = httpSession;
	}
	
	public SesionWS(WebSocketSession session) {
		this.websocketSession = session;
	}

	public SesionWS(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public String getNombre() {
		return nombre;
	}

	public WebSocketSession getSession() {
		return websocketSession;
	}

	public String getId() {
		return this.websocketSession.getId();
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	public HttpSession getHttpSession() {
		return httpSession;
	}
	
	public void setWebsocketSession(WebSocketSession websocketSession) {
		this.websocketSession = websocketSession;
	}
	
	public WebSocketSession getWebsocketSession() {
		return websocketSession;
	}
	
	public String getUserId() {
		return this.httpSession.getAttribute("user").toString();
	}	
	
}
