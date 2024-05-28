package edu.uclm.esi.tysweb2023.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Historial implements Serializable{

	@Id @Column(length = 36)
	private String id;
	
	@Column(nullable = false)
	private Date fecha;
	
	@Column(nullable = false)
	private String id_partida;
	
	@Column(nullable = false)
	private String juego;
	
	@Column(nullable = false)
	private String nickUsuario;
	
	@Column(nullable = false)
	private String mensaje;
	
	public Historial() {
		this.id = UUID.randomUUID().toString();
		this.fecha = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNickUsuario() {
		return nickUsuario;
	}
	
	public String getIdPartida() {
		return id_partida;
	}

	public void setNickUsuario(String nickUsuario) {
		this.nickUsuario = nickUsuario;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void setIdPartida(String idPartida) {
		this.id_partida = idPartida;
	}

	public String getId_partida() {
		return id_partida;
	}

	public void setId_partida(String id_partida) {
		this.id_partida = id_partida;
	}

	public String getJuego() {
		return juego;
	}

	public void setJuego(String juego) {
		this.juego = juego;
	}
	
	
}
