package edu.uclm.esi.tysweb2023.http;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import edu.uclm.esi.tysweb2023.model.Carta;
import edu.uclm.esi.tysweb2023.model.Historial;
import edu.uclm.esi.tysweb2023.model.Reloj;
import edu.uclm.esi.tysweb2023.model.Tablero;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.ws.ManagerWS;
import edu.uclm.esi.tysweb2023.ws.SesionWS;
import edu.uclm.esi.tysweb2023.ws.WSTablero;
import edu.uclm.esi.tysweb2023.ws.WebSocketClient;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("matches")
@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class MatchController {
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
    private WebSocketClient webSocketClient;
	
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
			
			//Comprobación pagos //usuarios logeados
			if(!user.getNombre().contains("Invitado") && juego.equals("Tablero4R")) {
				User usuLog = this.userDAO.findById(user.getId()).get();
				Integer numberOfMatches = usuLog.getPaidMatches();
				if (numberOfMatches==null || numberOfMatches==0) {
					throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No hay créditos para jugar");
				}
				if(numberOfMatches > 1 || numberOfMatches == 1) {
					usuLog.setPaidMatches(numberOfMatches - 1);
					this.userDAO.save(usuLog);
				}
			}
			
			UserController.httpSessions.put(session.getId(), session);
			ManagerWS.get().addSessionByUserId(user.getId(), session);
			
			ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
			result.put("httpId", session.getId());
			
			Tablero tableroJuego = this.matchService.newMatch(user,juego);
			result.put("tablero", tableroJuego);
			result.put("nickJugador", user.getNombre());
	
			//ROBOT 4 en raya
			if (juego.equals("Tablero4R")) {
	            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	            scheduler.schedule(() -> {
	                if (tableroJuego.getPlayers().size() == 1) {
	                    Reloj reloj = new Reloj(tableroJuego, matchService, session);
	                    System.out.println("[Robot] Está entrando en la partida.");
	                    Thread relojThread = new Thread(reloj);
	                    relojThread.start();
	                }
	            }, 30, TimeUnit.SECONDS);
			}
			
			//¿Partida lista?
			if (tableroJuego.checkPartidaLista()) {
				//Avisamos a los jugadores
				this.matchService.notificarEstado("START", tableroJuego.getId());
			}
			
			return result;
		} catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
			if(result.getPlayers().size() == 2) {
				if(result.getJugadorConElTurno().getId().equals(user.getId())) {
					control = 0;
				}else {
					control = 1;
				}
			}
		}
		return control;
	}
	
	/*Cartas*/
	@GetMapping("/obtenerManoJugador")
	public List<Carta> obtenerManoJugador(HttpSession session, @RequestParam String id){
		User user = (User) session.getAttribute("user");
		Tablero result = this.matchService.findMatch(id);
		if(result.getPlayers().get(0).getNombre().equals(user.getNombre())) {
			return result.getCartas1();
		}else {
			return result.getCartas2();
		}
	}
	
	@GetMapping("/obtenerCartasMesa")
	public List<Carta> obtenerCartasMesa(HttpSession session, @RequestParam String id){
		Tablero result = this.matchService.findMatch(id);
		return result.getCartasMesa();
	}
	
	@GetMapping("/checkPartidaLista")
	public boolean checkPartida(HttpSession session, @RequestParam String id){
		Tablero result = this.matchService.findMatch(id);
		return result.checkPartidaLista();
	}
	
	@GetMapping("/queJugadorSoy")
	public int queJugadorSoy(HttpSession session, @RequestParam String id){
		int jugador = 0;
		User user = (User) session.getAttribute("user");
		Tablero result = this.matchService.findMatch(id);
		if(result != null) {
			if(result.getPlayers().get(0).getNombre().equals(user.getNombre())) {
				jugador = 1;
			}else {
				jugador = 2;
			}
		}
		return jugador;
	}
	
	@GetMapping("/history")
    public List<Historial> getHistorial() {
        return matchService.getHistorial();
    }
	
	@GetMapping("/desconectar")
	public void desconectar(HttpSession session, @RequestParam String id){
		if(session.getAttribute("user") != null) {
			User us = (User) session.getAttribute("user");
			Tablero tbd = this.matchService.findMatch(id);
			tbd.getPlayers().remove(us);
		}
	}
	
}
