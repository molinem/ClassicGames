package edu.uclm.esi.tysweb2023.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {
		@Index(columnList = "email", unique = true)
})

public class User {
	@Id @Column(length = 36)
	private String id;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
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

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		//Encriptar contraseña
		this.pwd =  org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}

}
