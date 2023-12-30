package edu.uclm.esi.tysweb2023.model;

import java.util.Random;

public class AnonymousUser extends User {
	private String nombreUsuario;
	
 public AnonymousUser() {
	 super();
	 this.setNombre("Invitado"+ new Random().nextInt(101));
 }
 
}
