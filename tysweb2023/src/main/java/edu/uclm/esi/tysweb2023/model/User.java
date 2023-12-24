package edu.uclm.esi.tysweb2023.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.uclm.esi.tysweb2023.ws.ManagerWS;
import edu.uclm.esi.tysweb2023.ws.SesionWS;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(indexes = {
		@Index(columnList = "email", unique = true)
})

public class User implements Serializable {
	@Id @Column(length = 36)
	private String id;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String pwd;
	@Transient
	private SesionWS sesionWs;
	
	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		//Encriptar contraseña
		this.pwd =  org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}
	
	public SesionWS getSesionWS() {
		return this.sesionWs;
	}
	
	public void setSesionWS(SesionWS sesionWs) {
		this.sesionWs = sesionWs;
	}
	
	public void sendMessage(JSONObject jso) throws IOException {
		SesionWS ws = ManagerWS.get().getSessionByUserId(this.id);
		WebSocketSession wsSession = ws.getWebsocketSession();
		
		synchronized (wsSession) {
			TextMessage message = new TextMessage(jso.toString());
			try {
				wsSession.sendMessage(message);
			} catch (IOException e) {
				System.err.println("In notify: " +	wsSession.getId() + "-> " + e.getMessage());
			} 
		}
	}
	

}
