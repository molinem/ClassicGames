package edu.uclm.esi.tysweb2023.http;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.services.UserService;


@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public void register(@RequestBody Map<String,String> info) {
		String nombre = info.get("nombre").trim();
		String email = info.get("email").trim();
		String pwd1 = info.get("pwd1").trim();
		String pwd2 = info.get("pwd2").trim();
		
		if (nombre.length()<5) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El nombre debe tener al menos 5 caracteres");
		}

		if (pwd1.length()<5) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La contraseña debe tener al menos 5 caracteres");
		}
		
		if (!pwd2.equals(pwd1)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Las contraseñas no coinciden");
		}
		this.userService.register(nombre, email, pwd1);
	}
}
