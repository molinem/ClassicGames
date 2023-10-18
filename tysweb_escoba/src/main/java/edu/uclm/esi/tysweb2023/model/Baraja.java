package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
	 private List<Carta> listaCartas;
	 
	 public Baraja() {
		 listaCartas = new ArrayList<>();
		 crearBaraja();
	 }

	private void crearBaraja() {
		for(int i=0; i<10;i++) {
			listaCartas.add(new Carta(i,"ORO"));
			listaCartas.add(new Carta(i,"COPA"));
			listaCartas.add(new Carta(i,"ESPADAS"));
			listaCartas.add(new Carta(i,"BASTOS"));
		}
		//Barajar cartas
		Collections.shuffle(listaCartas);
	}
	
	
	public Carta obtenerCarta() {
		Carta cartaObtenida = listaCartas.get(0);
		listaCartas.remove(0);
		return cartaObtenida;
	}
	
	private boolean hayCartasDisponibles() {
		return listaCartas.size()>0;
	}
}
