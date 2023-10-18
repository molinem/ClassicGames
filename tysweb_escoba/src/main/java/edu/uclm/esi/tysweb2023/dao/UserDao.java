package edu.uclm.esi.tysweb2023.dao;

import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.tysweb2023.model.User;

public interface UserDAO extends CrudRepository<User, String> {
 
	User findByEmailAndPwd(String email, String pwd);
}
