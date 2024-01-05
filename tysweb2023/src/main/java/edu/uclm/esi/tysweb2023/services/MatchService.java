package edu.uclm.esi.tysweb2023.services;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;
import edu.uclm.esi.tysweb2023.model.Tablero;
import edu.uclm.esi.tysweb2023.model.User;

@Service
public class MatchService {

	private Map<String,Tablero> tableros = new HashMap<>();
	private List<Tablero> tablerosPendientes = new ArrayList<>();
	private boolean partidaLista;
		
	public Tablero newMatch(User user,String juego) throws Exception {
		Tablero tablero = null;
		
		//No hay tableros pendientes
		if (this.tablerosPendientes.isEmpty()) {
			
			Class<?> clazz;
			try {
				juego = "edu.uclm.esi.tysweb2023.model."+juego;
				clazz = Class.forName(juego);
			} catch (ClassNotFoundException e) {
				throw new Exception("[New Match] El juego no existe");
			}
			//Instanciar la  clase
			Constructor constructor = clazz.getConstructors()[0];
			try {
				tablero = (Tablero) constructor.newInstance();
			} catch (Exception e) {
				throw new Exception("[New Match] Contacta con el administrador");
			}
			
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

	public Tablero poner(String id, Map<String, Object> movimiento, String idUser) {
		Tablero tablero = this.tableros.get(id);
		if(tablero == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No encuentro esa partida");
		}
		try {
			tablero.poner(movimiento, idUser);
			
		}catch(MovimientoIlegalException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
		return tablero;
	}

	public Tablero findMatch(String id) {
		return this.tableros.get(id);
	}
	
	public void notificarEstado(String tipoMensaje, String idPartida) throws Exception {
		Tablero tb = this.findMatch(idPartida);
		List<User> jugadoresPartida = tb.getPlayers();
		JSONObject jso = new JSONObject();
		
		jso.put("type", tipoMensaje);
		jso.put("matchId", idPartida);
		jso.put("player_1", jugadoresPartida.get(0).getNombre());
		jso.put("player_2", jugadoresPartida.get(1).getNombre());
		jso.put("playerWithTurn", tb.getJugadorConElTurno().getNombre());

		
		if (tipoMensaje.contentEquals("START")) {
			try {
				jugadoresPartida.get(0).sendMessage(jso);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(tipoMensaje.contentEquals("WINNER")) {
			JSONObject jsoWinner = new JSONObject();
			jsoWinner.put("type", tipoMensaje);
			jsoWinner.put("winner", this.findMatch(idPartida).getGanador());
			String nick_ganador="";
			
			if (tb.getUltimoColor() == 'R') {
				nick_ganador = jugadoresPartida.get(0).getNombre();
			}else {
				nick_ganador = jugadoresPartida.get(1).getNombre();
			}
			jsoWinner.put("ganador", nick_ganador);
			difundirMsg(jsoWinner,jugadoresPartida);
		}else {
			difundirMsg(jso,jugadoresPartida);
		}
	}
	
	private void difundirMsg(JSONObject jsa, List<User> jugadoresPartida ) throws Exception {
		for (User player : jugadoresPartida) {
			try {
				player.sendMessage(jsa);
			} catch (IOException e) {
				throw new Exception("[Notificar Estado] Se ha producido el siguiente error: " + e.getMessage());
			}
		}
	}
	
	public void notificarMovimiento(String idPartida, JSONObject jso) throws Exception {	
		List<User> jugadoresPartida = this.findMatch(idPartida).getPlayers();
		
		for (User player : jugadoresPartida) {
			try {
				player.sendMessage(jso);
			} catch (IOException e) {
				throw new Exception("[Notificar Estado] Se ha producido el siguiente error: " + e.getMessage());
			}
		}	
	}
	
	
}
