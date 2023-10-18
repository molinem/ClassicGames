package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Jugador {
	@Id @Column(length = 36)
	private String id;
	private String nombre;
	private boolean turno;
	private int puntos;
	private List<Carta> cartas;
	
	public Jugador(String nombre) {
		this.nombre = nombre;
		this.puntos = 0;
		cartas = new ArrayList<Carta>();
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

	public boolean isTurno() {
		return turno;
	}

	public void setTurno(boolean turno) {
		this.turno = turno;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public List<Carta> getCartas() {
		return cartas;
	}

	public void setCartas(List<Carta> cartas) {
		this.cartas = cartas;
	}
	
	public void addCartas(List<Carta> cartasRobadas ) {
		cartas.addAll(cartasRobadas);
	}
	
}
