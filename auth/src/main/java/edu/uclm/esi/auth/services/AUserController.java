package edu.uclm.esi.auth.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.auth.services.AUserService;
import jakarta.servlet.http.HttpSession;

public class AUserController {
	
	@Autowired
	private AUserService userService;
	
	@PutMapping("/alogin")
	public HashMap<String, Object> login(HttpSession session,
			@RequestBody Map<String, Object> info) {
		String name = info.get("name").toString().trim();
		String pwd = info.get("pwd").toString().trim();
		String userId = this.userService.login(name, pwd);
		if (userId==null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Credenciales inválidas");
		HashMap<String, Object> result = new HashMap<>();
		result.put("userId", userId);
		result.put("httpSessionId", session.getId());
		return result;
	}	
}
