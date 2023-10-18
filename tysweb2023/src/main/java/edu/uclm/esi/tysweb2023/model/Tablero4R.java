package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import lombok.Data;


public class Tablero4R {

	private char[][] casillas = new char[6][7];
	private String id;
	private char ultimoColor;
	private char ganador;

	private List<User> players;
	private User jugadorConElTurno;


	public Tablero4R() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
	}
	
	
	public char[][] getCasillas() {
		return casillas;
	}


	public String getId() {
		return id;
	}


	public void poner(int columna, String idUser) throws MovimientoIlegalException {
		
		if(this.ganador != Character.MIN_VALUE) {
			throw new MovimientoIlegalException("La partida ha finalizado");
		}
		
		if(!this.jugadorConElTurno.getId().equals(idUser))
			throw new MovimientoIlegalException("No es tu turno");

		
		char [] col = new char[6];
		for (int i=5; i>=0; i--) {
			col[i]= this.casillas[i][columna];
			
		}
		
		if (col[0]!='\0')
			throw new MovimientoIlegalException("La columna está llena");
		
		for (int i=5; i>=0; i--) {
			if(this.casillas[i][columna]=='\0')
				this.casillas[i][columna] = this.ultimoColor;
				this.ultimoColor = this.ultimoColor=='R' ? 'A' : 'R';
				
				if (this.jugadorConElTurno == this.players.get(0)) {
				    this.jugadorConElTurno = this.players.get(1);
				} else {
				    this.jugadorConElTurno = this.players.get(0);
				}
				
				break;
		}
	}


	private void comprobarFin() {
		//this.ganador = this.ultimoColor;
		
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

	
	public void iniciar() {
		this.jugadorConElTurno = this.players.get(new Random().nextInt(this.players.size()));
		this.ultimoColor = 'R';
		
	}
}
