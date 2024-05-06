
package edu.uclm.esi.tysweb2023.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.tysweb2023.exceptions.MovimientoIlegalException;

public class Escoba extends Tablero {

	private Baraja baraja;
	private List<Carta> cartas1, cartas2, mesa;
	private int oros1, numCartas1, sietes1, sieteOros1, escobas1;
	private int oros2, numCartas2, sietes2, sieteOros2, escobas2;

	public final static int OROS = 0, COPAS = 1, ESPADAS = 2, BASTOS = 3;

	@Override
	public void iniciar() {
		this.baraja = new Baraja();
		this.baraja.barajar();
		this.cartas1 = new ArrayList<>();
		this.cartas2 = new ArrayList<>();
		this.mesa = new ArrayList<>();

		for (int i = 0; i < 3; i++)
			this.cartas1.add(this.baraja.pop());

		for (int i = 0; i < 3; i++)
			this.cartas2.add(this.baraja.pop());

		for (int i = 0; i < 4; i++)
			this.mesa.add(this.baraja.pop());

		this.jugadorConElTurno = this.players.get(0);

	}

	@Override
	public void poner(Map<String, Object> movimiento, String idUser) throws MovimientoIlegalException {

		Map<String, Object> mia = convertirListaAMap((ArrayList<Map<String, Object>>)movimiento.get("mia"));
		List<Map<String, Object>> cartasMesa = (List<Map<String, Object>>) movimiento.get("mesa");
		
		
		Carta cartaQuitada = this.quitar(idUser, mia);
		ArrayList<Carta> cartasQuitadasDeMesa = this.quitarDeMesa(cartasMesa);
		
		
		if (cartasQuitadasDeMesa.isEmpty()) {
			this.mesa.add(cartaQuitada);

		} else {
			this.actualizarCuentas(idUser, cartaQuitada, cartasQuitadasDeMesa);
		}
		
	}
	
	public static Map<String, Object> convertirListaAMap(ArrayList<Map<String, Object>> lista) {
        if (lista != null && !lista.isEmpty()) {
            return lista.get(0);
        } else {
            return new HashMap<>();
        }
    }

	private int numeroDeOros(Carta cartaQuitada, ArrayList<Carta> cartasQuitadasDeMesa) {
		int contadorOros = 0;

		// Verifica si la carta quitada es de oros
		if (cartaQuitada.getPalo() == OROS) {
			contadorOros++;
		}

		// Cuenta las cartas de oros quitadas de la mesa
		for (Carta carta : cartasQuitadasDeMesa) {
			if (carta.getPalo() == OROS) {
				contadorOros++;
			}
		}
		return contadorOros;
	}

	private int numeroDeSietes(Carta cartaQuitada, ArrayList<Carta> cartasQuitadasDeMesa) {
		int contadorSietes = 0;

		// Verifica si la carta quitada es un siete
		if (cartaQuitada.getValor() == 7) {
			contadorSietes++;
		}

		// Cuenta los sietes quitados de la mesa
		for (Carta carta : cartasQuitadasDeMesa) {
			if (carta.getValor() == 7) {
				contadorSietes++;
			}
		}
		return contadorSietes;
	}

	private void actualizarCuentas(String idUser, Carta cartaQuitada, ArrayList<Carta> cartasQuitadasDeMesa) {

		if (idUser.equals(this.players.get(0).getId())) {

			this.oros1 = this.oros1 + this.numeroDeOros(cartaQuitada, cartasQuitadasDeMesa);
			this.sietes1 = this.sietes1 + this.numeroDeSietes(cartaQuitada, cartasQuitadasDeMesa);
			this.numCartas1 = this.numCartas1 + 1 + cartasQuitadasDeMesa.size();

			// 7 de oros
			if (mesa.isEmpty() && !this.baraja.isEmpty())
				this.escobas1++;

		} else {
			this.oros2 = this.oros2 + this.numeroDeOros(cartaQuitada, cartasQuitadasDeMesa);
			this.sietes2 = this.sietes2 + this.numeroDeSietes(cartaQuitada, cartasQuitadasDeMesa);
			this.numCartas2 = this.numCartas2 + 1 + cartasQuitadasDeMesa.size();

			// 7 de oros
			if (mesa.isEmpty() && !this.baraja.isEmpty())
				this.escobas2++;
		}

	}

	private ArrayList<Carta> quitarDeMesa(List<Map<String, Object>> cartasElegidas) {
	    ArrayList<Carta> cartasQuitadas = new ArrayList<>();
	    for (int i = 0; i < cartasElegidas.size(); i++) {
	        Map<String, Object> cartaElegida = cartasElegidas.get(i);
	        int palo = (int) cartaElegida.get("palo");
	        int valor = (int) cartaElegida.get("valor");
	        boolean seleccionada = cartaElegida.containsKey("seleccionada") && (boolean) cartaElegida.get("seleccionada");

	        for (int j = 0; j < this.mesa.size(); j++) {
	            Carta cartaMesa = this.mesa.get(j);
	
	            if (cartaMesa.getPalo() == palo && cartaMesa.getValor() == valor && seleccionada) {
	                cartasQuitadas.add(this.mesa.remove(j));
	                break;
	            }
	        }
	    }
	    System.out.println("Cartas quitadas: " + cartasQuitadas.toString());
	    return cartasQuitadas;
	}


	private Carta quitar(String idUser, Map<String, Object> mia) {

		User player;
		List<Carta> cartas;

		if (idUser.equals(this.players.get(0).getId())) {
			player = this.players.get(0);
			cartas = this.cartas1;
		} else {
			player = this.players.get(1);
			cartas = this.cartas2;
		}

		int palo = (int) mia.get("palo");
		int valor = (int) mia.get("valor");
		
		boolean seleccionada = mia.containsKey("seleccionada") && (boolean) mia.get("seleccionada");

		for (int i = 0; i < cartas.size(); i++) {
	        Carta carta = cartas.get(i);
	        if (carta.getPalo() == palo && carta.getValor() == valor && seleccionada) {
	            return cartas.remove(i);
	        }
	    }
		return null;
	}


	public List<Carta> getCartas1() {
		return cartas1;
	}

	public List<Carta> getCartas2() {
		return cartas2;
	}

	@Override
	public List<Carta> getCartasMesa() {
		return mesa;
	}
	
	@Override
	protected void comprobarListo() {
		// TODO Auto-generated method stub

	}

	@Override
	public char[][] getCasillas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONArray mostrarCasillas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getGanador() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getUltimoColor() {
		// TODO Auto-generated method stub
		return 0;
	}



}