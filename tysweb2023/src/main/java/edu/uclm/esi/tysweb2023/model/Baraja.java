package edu.uclm.esi.tysweb2023.model;

import java.util.Random;

import java.util.Stack;

public class Baraja {

	private Stack<Carta> cartas = new Stack<>();

	public final static int OROS = 0, COPAS = 1, ESPADAS = 2, BASTOS = 3;

public Baraja() {

		for (int palo = OROS; palo <= BASTOS; palo++) {

			for (int valor = 1; valor <= 10; valor++) {

				Carta carta = new Carta();

				carta.setPalo(palo);

				carta.setValor(valor);

				this.cartas.add(carta);

			}

		}

	}

public void barajar() {

		Random dado = new Random();

		for (int i = 0; i < 100; i++) {

			int a = dado.nextInt(this.cartas.size());

			int b = dado.nextInt(this.cartas.size());

			Carta aux = this.cartas.get(a);

			this.cartas.set(a, this.cartas.get(b));

			this.cartas.set(b, aux);

		}

	}

public Carta pop() {

	return this.cartas.pop();

	}

public boolean isEmpty() {
         return this.cartas.isEmpty();

        }
}