package edu.uclm.esi.tysweb2023.model;

import java.util.Iterator;
import java.util.UUID;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import lombok.Data;


public class Tablero4R {

	private char[][] casillas = new char[6][7];
	private String id;
	private char ultimoColor;
	private char ganador;


	public Tablero4R() {
		this.id = UUID.randomUUID().toString();
	}
	
	
	public char[][] getCasillas() {
		return casillas;
	}


	public String getId() {
		return id;
	}


	public void poner(int columna, char color) throws MovimientoIlegalException {
		
		if(this.ganador != Character.MIN_VALUE) {
			throw new MovimientoIlegalException("La partida ha finalizado");
		}
		
		if(color == this.ultimoColor) {
			throw new MovimientoIlegalException("No es tu turno");
		}
		
		for (int i=5;i>=0;i--) {
			if(this.casillas[i][columna]!='R' && this.casillas[i][columna]!='A') {
				this.casillas[i][columna] = color;
				this.ultimoColor = color;
				this.comprobarFin();
				return;
			}
		}
		throw new MovimientoIlegalException("La columna está llena");
	}


	private void comprobarFin() {
		this.ganador = this.ultimoColor;
		
	}
}
