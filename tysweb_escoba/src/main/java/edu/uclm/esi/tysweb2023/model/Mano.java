package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.List;

public class Mano {
	
	private Jugador jugador;
	private List<Carta> cartas_mano;
	
	public Mano(Jugador jugador) {
		cartas_mano = new ArrayList<>();
	}
	
	public void setCarta_1(Carta c) {
		cartas_mano.set(0, c);
	}
	
	public void setCarta_2(Carta c) {
		cartas_mano.set(1, c);
	}
	
	public void setCarta_3(Carta c) {
		cartas_mano.set(2, c);
	}
}
