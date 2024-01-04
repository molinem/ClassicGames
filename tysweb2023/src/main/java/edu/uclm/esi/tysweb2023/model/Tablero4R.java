package edu.uclm.esi.tysweb2023.model;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;


public class Tablero4R extends Tablero{

	private char[][] casillas = new char[6][7];
	private char ultimoColor;
	private char ganador;


	public Tablero4R() {
		super();
	}
	
	
	public char[][] getCasillas() {
		return casillas;
	}
	
	public JSONArray mostrarCasillas() {
		JSONArray jso = new JSONArray();
        for (int i = 0; i < casillas.length; i++) {
            JSONArray jsonRow = new JSONArray();
            for (int j = 0; j < casillas[i].length; j++) {
                jsonRow.put(Character.toString(casillas[i][j]));
            }
            jso.put(jsonRow);
        }
        return jso;
	}

	public void poner(Map<String, Object> movimiento, String idUser) throws MovimientoIlegalException {
		
		int columna = (int) movimiento.get("col");
		
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
			if(this.casillas[i][columna]=='\0') {
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
		
	}


	private void comprobarFin() {
		//this.ganador = this.ultimoColor;
		
	}

	
	public void iniciar() {
		this.jugadorConElTurno = this.players.get(new Random().nextInt(this.players.size()));
		this.ultimoColor = 'R';
	}
	
	@Override
	protected void comprobarListo() {
		this.preparado = this.players.size()==2;
		if (this.preparado)
			this.jugadorConElTurno = new SecureRandom().nextBoolean() ? this.players.get(0) : this.players.get(1);
	}

}
