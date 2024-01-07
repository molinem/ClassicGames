package edu.uclm.esi.tysweb2023.http;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.tysweb2023.dao.UserDAO;
import edu.uclm.esi.tysweb2023.model.AnonymousUser;
import edu.uclm.esi.tysweb2023.model.Tablero;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.ws.ManagerWS;
import edu.uclm.esi.tysweb2023.ws.SesionWS;
import edu.uclm.esi.tysweb2023.ws.WSTablero;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("matches")
@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class MatchController {
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private UserDAO userDAO;
		
	//start?juego=Cartas
	@GetMapping("/start")
	public ConcurrentHashMap<String, Object> start(HttpSession session, @RequestParam String juego) {
		try {
			User user = (User) session.getAttribute("user");	
			if (user == null) {
				user = new AnonymousUser();
				session.setAttribute("user", user);
			}
			
			ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
			result.put("httpId", session.getId());
			
			Tablero tableroJuego = this.matchService.newMatch(user,juego);
			result.put("tablero", tableroJuego);
			result.put("nickJugador", user.getNombre());
			
			UserController.httpSessions.put(session.getId(), session);
			ManagerWS.get().addSessionByUserId(user.getId(), session);
			
			//¿Partida lista?
			if (tableroJuego.checkPartidaLista()) {
				//Avisamos a los jugadores
				this.matchService.notificarEstado("START", tableroJuego.getId());
			}
			return result;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	@PostMapping("/poner")
	public Tablero poner(HttpSession session, @RequestBody Map<String,Object> info) {
		String id = info.get("id").toString();
		User user = (User) session.getAttribute("user");
		Tablero tb = null;
		if(this.matchService.findMatch(id).getGanador() == Character.MIN_VALUE) {
			tb = this.matchService.poner(id, info, user.getId());
		}
		return tb;
	}
	
	@GetMapping("/meToca")
	public int meToca(HttpSession session,@RequestParam String id) {
		/* 0 -> Es tu turno
		 * 1 -> No es tu turno
		 * 2 -> Partida no lista
		*/
		int control = 2;
		User user = (User) session.getAttribute("user");
		Tablero result = this.matchService.findMatch(id);
		if(result != null) {
			if(result.getJugadorConElTurno().getId().equals(user.getId())) {
				control = 0;
			}else {
				control = 1;
			}
		}
		return control;
	}
}
