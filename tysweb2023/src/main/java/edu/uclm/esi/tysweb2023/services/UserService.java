package edu.uclm.esi.tysweb2023.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.tysweb2023.dao.UserDAO;
import edu.uclm.esi.tysweb2023.model.Email;
import edu.uclm.esi.tysweb2023.model.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	

	public void register(String nombre, String email, String pwd1) {
		User user = new User();
		user.setNombre(nombre);
		user.setEmail(email);
		user.setPwd(pwd1);
		
		this.userDAO.save(user);
		Email smtp = new Email();
		smtp.send(user.getEmail(),"Nuevo registro usuario","Se ha registrado correctamente en la aplicación");
	}

	public User login(String email, String pwd) {
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		return this.userDAO.findByEmailAndPwd(email, pwd);
	}

	public void borrarCuenta(String userId) {
		this.userDAO.deleteById(userId);
	}
	
	public Optional<User> obtenerInformacion(String idUser) {
		return this.userDAO.findById(idUser);
	}
	
	public void addMatches(String userId, Integer matches) {
		User user = this.userDAO.findById(userId).get();
		Integer paidMatches = user.getPaidMatches();
		if (paidMatches==null)
			paidMatches = 0;
		user.setPaidMatches(paidMatches + matches);
		this.userDAO.save(user);
	}
}
