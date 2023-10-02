package edu.uclm.esi.tysweb2023.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.tysweb2023.dao.UserDao;
import edu.uclm.esi.tysweb2023.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public User login(String email, String pwd) {
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		return this.userDao.findByEmailAndPwd(email, pwd);
		}
	
	public void register(String nombre, String email, String pwd1) {
		User user= new User();
		user.setEmail(email);
		user.setNombre(nombre);
		user.setPwd(pwd1);
		
		
		this.userDao.save(user);
		

		}

	public void borrarCuenta(String userId) {
		this.userDao.deleteById(userId);
		
	}
		
		
	}


