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
	
	private int contador;
	
	//start?juego=Cartas
	@GetMapping("/start")
	public ConcurrentHashMap<String, Object> start(HttpSession session, @RequestParam String juego) {
		try {
			User user = (User) session.getAttribute("user");	
			if (user == null) {
				user = new AnonymousUser();
				contador++;
				user.setNombre("Invitado_"+contador);
				session.setAttribute("user", user);
			}
			
			ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
			result.put("httpId", session.getId());
			
			Tablero tableroJuego = this.matchService.newMatch(user,juego);
			result.put("tablero", tableroJuego);
			UserController.httpSessions.put(session.getId(), session);
			ManagerWS.get().addSessionByUserId(session.getId(), session);
			
			return result;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	@PostMapping("/poner")
	public Tablero poner(HttpSession session,@RequestBody Map<String,Object> info) {
		String id = info.get("id").toString();
		User user = (User) session.getAttribute("user");
		WebSocketSession ws =  user.getSesionWS().getSession();
		
		return this.matchService.poner(id, info, user.getId());
	}
	
	@GetMapping("/meToca")
	public boolean meToca(HttpSession session,@RequestParam String id) {
		User user = (User) session.getAttribute("user");	
		Tablero result = this.matchService.findMatch(id);
		return result.getJugadorConElTurno().getId().equals(user.getId());
	}
}
