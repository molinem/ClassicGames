package edu.uclm.esi.tysweb2023.http;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.tysweb2023.model.Tablero4R;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.services.UserService;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("users")
@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true", allowedHeaders = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	public static ConcurrentHashMap<String, HttpSession> httpSessions = new ConcurrentHashMap<>();
	
	@PostMapping("/login")
	public Map<String, Object> login(HttpSession session, @RequestBody Map<String, String> info) {
		String email = info.get("email").trim();
		String pwd = info.get("pwd").trim();
		User user = this.userService.login(email, pwd);
		if (user==null) 
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas");
		session.setAttribute("user", user);
		session.setAttribute("userId", user.getId());
		Map<String, Object> result = new HashMap<>();
		result.put("httpId", session.getId());
		httpSessions.put(session.getId(),session);
		return result;
	}
	

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
		
		
		try {
			this.userService.register(nombre, email, pwd1);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ese correo electrónico ya existe");
		}
		
	}
	
	
	@GetMapping("/solicitarBorrado/{email}")
	public String solicitarBorrado(HttpSession session, @PathVariable String email,@RequestParam String pwd) {
		User user = this.userService.login(email, pwd);
		if (user == null) 
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidos");
		
		session.setAttribute("userId", user.getId());
		return "¿Estás seguro de que quieres eliminar tu cuenta?";
	}
	
	
	@DeleteMapping("/borrarCuenta")
	public void borrarCuenta(HttpSession session, @RequestParam boolean respuesta) {
		if (respuesta) {
			String userId = session.getAttribute("userId").toString();
			this.userService.borrarCuenta(userId);
			session.invalidate();
		}else {
			session.removeAttribute("userId");
		}
	}
}
