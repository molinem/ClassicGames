package edu.uclm.esi.tysweb2023.services;


import org.springframework.stereotype.Service;

import edu.uclm.esi.tysweb2023.model.User;

@Service
public class UserService {

	public void register(String nombre, String email, String pwd1) {
		User user = new User();
		user.setNombre(nombre);
		user.setEmail(email);
		user.setPwd(pwd1);
	}

}
