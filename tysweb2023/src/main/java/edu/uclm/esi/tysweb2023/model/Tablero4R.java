package edu.uclm.esi.tysweb2023.model;

import java.security.SecureRandom;
import java.util.List;
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

	public char getGanador() {
		return this.ganador;
	}
	
	public char getUltimoColor() {
		return this.ultimoColor;
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
		
		int filaRecienColocada = -1;
		for (int i=5; i>=0; i--) {
			if(this.casillas[i][columna]=='\0') {
				this.casillas[i][columna] = this.ultimoColor;
				filaRecienColocada = i;
				this.ultimoColor = this.ultimoColor=='R' ? 'A' : 'R';
				
				if (this.jugadorConElTurno == this.players.get(0)) {
				    this.jugadorConElTurno = this.players.get(1);
				} else {
				    this.jugadorConElTurno = this.players.get(0);
				}
				break;
			}
		}
		
		if (filaRecienColocada != -1 && comprobarGanador(filaRecienColocada, columna)) {
	        this.ganador = this.ultimoColor == 'R' ? 'A' : 'R'; // Ajustamos el color porque se cambió después de colocar la ficha
	    }
		
	}


	private boolean comprobarGanador(int filaRecienColocada, int columnaRecienColocada) {
	    final int NUMERO_PARA_GANAR = 4;
	    char ficha = this.casillas[filaRecienColocada][columnaRecienColocada];

	    // Verificación horizontal
	    int contador = 0;
	    for (int j = 0; j < this.casillas[0].length; j++) {
	        contador = (this.casillas[filaRecienColocada][j] == ficha) ? contador + 1 : 0;
	        if (contador >= NUMERO_PARA_GANAR) return true;
	    }

	    // Verificación vertical
	    contador = 0;
	    for (int i = 0; i < this.casillas.length; i++) {
	        contador = (this.casillas[i][columnaRecienColocada] == ficha) ? contador + 1 : 0;
	        if (contador >= NUMERO_PARA_GANAR) return true;
	    }

	    // Verificación diagonal (de arriba izquierda a abajo derecha)
	    contador = 0;
	    for (int i = 0; i < this.casillas.length; i++) {
	        int j = columnaRecienColocada - filaRecienColocada + i;
	        if (j >= 0 && j < this.casillas[0].length) {
	            contador = (this.casillas[i][j] == ficha) ? contador + 1 : 0;
	            if (contador >= NUMERO_PARA_GANAR) return true;
	        }
	    }

	    // Verificación diagonal (de abajo izquierda a arriba derecha)
	    contador = 0;
	    for (int i = this.casillas.length - 1; i >= 0; i--) {
	        int j = columnaRecienColocada + filaRecienColocada - i;
	        if (j >= 0 && j < this.casillas[0].length) {
	            contador = (this.casillas[i][j] == ficha) ? contador + 1 : 0;
	            if (contador >= NUMERO_PARA_GANAR) return true;
	        }
	    }

	    // No se encontró ganador
	    return false;
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


	@Override
	public List<Carta> getCartas1() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Carta> getCartas2() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Carta> getCartasMesa() {
		// TODO Auto-generated method stub
		return null;
	}

}
