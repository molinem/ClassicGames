package edu.uclm.esi.tysweb2023.model;

public class Carta {
	private String palo;
	private String imagen;
	private int valor;
	private boolean seleccionada;
	
	public Carta(int valor, String palo){
		this.valor = valor;
		this.palo = palo;
		this.seleccionada = false;
        //imagen
	}

	public String getPalo() {
		return palo;
	}

	public void setPalo(String palo) {
		this.palo = palo;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public boolean isSeleccionada() {
		return seleccionada;
	}

	public void setSeleccionada(boolean seleccionada) {
		this.seleccionada = seleccionada;
	}
	
	
}
