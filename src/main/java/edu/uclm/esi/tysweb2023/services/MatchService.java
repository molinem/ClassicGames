package edu.uclm.esi.tysweb2023.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import edu.uclm.esi.tysweb2023.model.Tablero4R;

@Service
public class MatchService {

	private Map<String,Tablero4R> tableros = new HashMap<>();
	
	public Tablero4R newMatch() {
		Tablero4R tablero = new Tablero4R();
		this.tableros.put(tablero.getId(),tablero);
		return tablero;
	}

	public Tablero4R poner(String id, int columna, char color) {
		Tablero4R tablero = this.tableros.get(id);
		if(tablero == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No encuentro esa partida");
		}
		try {
			tablero.poner(columna, color);
		}catch(MovimientoIlegalException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
		
		return tablero;
	}

}
