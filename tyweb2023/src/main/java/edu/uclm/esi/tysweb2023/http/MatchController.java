package edu.uclm.esi.tysweb2023.http;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.services.MatchService;


@RestController
@RequestMapping("matches")
public class MatchController {
	
	@Autowired
	private MatchService matchService;
	
	@GetMapping("/start")
	public Tablero4R start() {
		Tablero4R result = this.matchService.newMatch();
		return result;
	}
	
	@PostMapping("/poner")
	public Tablero4R poner(@RequestBody Map<String,Object> info) {
		String id = info.get("id").toString();
		int columna = (int) info.get("columna");
		char color = info.get("color").toString().charAt(0);
		return this.matchService.poner(id, columna, color);

		
	}
}
