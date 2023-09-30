package edu.uclm.esi.auth.services;


import org.springframework.stereotype.Service;

import edu.uclm.esi.auth.entities.AUser;
import edu.uclm.esi.auth.services.AUserService;

@Service
public class AUserService {

	public void register(String nombre, String email, String pwd1) {
		AUser user = new AUser();
		user.setNombre(nombre);
		user.setEmail(email);
		user.setPwd(pwd1);
	}

}
