package edu.uclm.esi.tysweb2023.http;


import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.dao.UserDAO;
import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("matches")
@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class MatchController {
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private UserDAO userDAO;
	
	
	@GetMapping("/start")
	public Tablero4R start(HttpSession session) {
		User user = (User) session.getAttribute("user");	
		String idUser = user.getId().toString();
		Optional<User> optUser = this.userDAO.findById(idUser);
		Tablero4R result = this.matchService.newMatch(optUser.get());
		return result;
	}
	
	@PostMapping("/poner")
	public Tablero4R poner(HttpSession session,@RequestBody Map<String,Object> info) {
		String id = info.get("id").toString();
		int columna = (int) info.get("columna");
		String idUser = session.getAttribute("idUser").toString();
		return this.matchService.poner(id, columna, idUser);
	}
	
	@GetMapping("/meToca")
	public boolean meToca(HttpSession session,@RequestParam String id) {
		String idUser = session.getAttribute("idUser").toString();
		Tablero4R result = this.matchService.findMatch(id);
		return result.getJugadorConElTurno().getId().equals(idUser);
	}
}
