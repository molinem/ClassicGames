package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;

public abstract class Tablero {
	private String id;

	protected List<User> players;
	protected User jugadorConElTurno;
	
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
	
	
	public List<User> getPlayers(){
		return players;
	}

	
	public User getJugadorConElTurno() {
		return jugadorConElTurno;
	}
	public abstract void iniciar();
	
	public abstract void poner(Map<String,Object> movimiento, String idUser) throws MovimientoIlegalException;
}
