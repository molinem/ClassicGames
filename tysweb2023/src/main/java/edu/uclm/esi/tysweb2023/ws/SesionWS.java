package edu.uclm.esi.tysweb2023.ws;

import org.springframework.web.socket.WebSocketSession;

/**
 * Clase que actúa como wrapper
 */
public class SesionWS {
	
	private String nombre;
	private WebSocketSession session;
	
	public SesionWS(String nombre, WebSocketSession session) {
		this.nombre = nombre;
		this.session = session;
	}

	public String getNombre() {
		return nombre;
	}


	public WebSocketSession getSession() {
		return session;
	}

	public String getId() {
		return this.session.getId();
	}
	
		
	
}
