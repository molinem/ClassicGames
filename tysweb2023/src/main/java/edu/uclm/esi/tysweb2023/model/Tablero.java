package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import jakarta.persistence.Transient;

public abstract class Tablero {
	private String id;
	@Transient
	protected List<User> players;
	@Transient
	protected User jugadorConElTurno;
	@Transient
	protected boolean preparado;
	
	public Tablero() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
	}
	

	public String getId() {
		return id;
	}
	
	public void addUser(User user) {
		this.players.add(user);		
	}
	
	public boolean partidaLista() {
		return preparado;
	}
	
	public boolean checkPartidaLista() {
		if (this.getPlayers().size() == 2) {
			preparado = true;
		}else {
			preparado = false;
		}
		return preparado;
	}
	
	public List<User> getPlayers(){
		return players;
	}

	public User getJugadorConElTurno() {
		return jugadorConElTurno;
	}
	public abstract void iniciar();
	protected abstract void comprobarListo();
	
	public abstract void poner(Map<String,Object> movimiento, String idUser) throws MovimientoIlegalException;
	public abstract char[][] getCasillas();
	public abstract JSONArray mostrarCasillas();
}
