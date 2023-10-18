package edu.uclm.esi.tysweb2023.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.model.User;

@Service
public class MatchService {

	private Map<String,Tablero4R> tableros = new HashMap<>();
	private List<Tablero4R> tablerosPendientes = new ArrayList<>();
	
	public Tablero4R newMatch(User user) {
		Tablero4R tablero;
		if (this.tablerosPendientes.isEmpty()) {
			tablero = new Tablero4R();
			tablero.addUser(user);
			this.tablerosPendientes.add(tablero);
		}else {
			tablero = this.tablerosPendientes.remove(0);
			tablero.addUser(user);
			tablero.iniciar();
			this.tableros.put(tablero.getId(),tablero);
		}
		
		return tablero;
	}

	public Tablero4R poner(String id, int columna, String idUser) {
		Tablero4R tablero = this.tableros.get(id);
		if(tablero == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No encuentro esa partida");
		}
		try {
			tablero.poner(columna, idUser);
		}catch(MovimientoIlegalException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		
		return tablero;
	}

	public Tablero4R findMatch(String id) {
		return this.tableros.get(id);
	}

}
