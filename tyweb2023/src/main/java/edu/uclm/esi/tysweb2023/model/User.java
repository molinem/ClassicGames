package edu.uclm.esi.tysweb2023.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
	private String id;
	private String nombre;
	private String email;
	private String pwd;
	
	public User() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		//Encriptar contraseña
		this.pwd =  org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}

}
