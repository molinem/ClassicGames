package edu.uclm.esi.tysweb2023.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.catalina.Manager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.tysweb2023.http.UserController;
import edu.uclm.esi.tysweb2023.model.Tablero;
import edu.uclm.esi.tysweb2023.model.User;
import edu.uclm.esi.tysweb2023.services.MatchService;
import jakarta.servlet.http.HttpSession;

@Component
public class WSTablero extends TextWebSocketHandler {
	
	private List<WebSocketSession> sessions = new ArrayList<>();
	private ConcurrentHashMap<String, SesionWS> sessionsByNombre = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, SesionWS> sessionsById = new ConcurrentHashMap<>();
	
	public String obtenerHttpId(WebSocketSession session) {
		HttpHeaders headers = session.getHandshakeHeaders();
		Collection<List<String>> values = headers.values();
		String httpSessionId = null;
		for (List<String> value : values) {
			for (String cadena : value) {
				if (cadena.startsWith("JSESSIONID")) {
					httpSessionId = cadena.substring(11);
					break;
				}else if(cadena.contains("JSESSIONID")) {
					int pos = cadena.indexOf("JSESSIONID");
					httpSessionId = cadena.subSequence(pos, cadena.length()).toString();
					httpSessionId = httpSessionId.substring(11);
					break;
				}
			}
			if (httpSessionId!=null)
				break;
		}
		return httpSessionId;
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception{				
		String httpSessionId = obtenerHttpId(session);
		if (httpSessionId != null) {
			ManagerWS.get().setWebsocketSession(httpSessionId, session);
		}else {
			System.out.printf("[AfterConnectionEstablished] >> %s \n","No se pudo obtener el httpId");
			
		}
	}
	
	public static String generateHttpId() {
        return UUID.randomUUID().toString();
    }
		
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload());
		String type = jso.getString("type");
		//Match update
		MatchService ms = ManagerWS.get().getMatchService();
		switch(type) {
			//JUEGOS
			case "MOVEMENT":
				String matchId = jso.getString("matchId");
				int columna = jso.getInt("col");
				
				//Sacar de la session el nombre del jugador
				User user = (User) ManagerWS.get().getSessionByWebSocketId(session.getId())
					.getHttpSession().getAttribute("user");

				String personaActualizarTablero = obtenerSiguienteNombre(ms.findMatch(matchId).getPlayers(),user.getNombre());
				Tablero tb = ms.findMatch(matchId);
				
				JSONArray casillas = tb.mostrarCasillas();
				jso = new JSONObject();
				jso.put("type", "MATCH UPDATE");
				jso.put("player", personaActualizarTablero);
				jso.put("matchId", matchId);
				jso.put("board", casillas);
				
				if (ms.findMatch(matchId).getGanador() != Character.MIN_VALUE) {
					char ganador = ms.findMatch(matchId).getGanador();
					jso.put("winner", ganador);
					List<User> jugadoresPartida = tb.getPlayers();
					String nick_ganador="";
					
					if (ganador == 'R') {
						nick_ganador = jugadoresPartida.get(0).getNombre();
					}else {
						nick_ganador = jugadoresPartida.get(1).getNombre();
					}
					jso.put("nickWinner", nick_ganador);
					ms.almacenarEstadistica(matchId, "4 en raya", nick_ganador, "Gana la partida");
				}
				ms.notificarMovimiento(matchId, jso);
				break;
			case "MOVEMENTCARTA":
				String matchId_Cartas = jso.getString("matchId");
				
				//Sacar de la session el nombre del jugador				
				User userCartas = (User) ManagerWS.get().getSessionByWebSocketId(session.getId())
						.getHttpSession().getAttribute("user");

				String personaActualizarTableroCartas = obtenerSiguienteNombre(ms.findMatch(matchId_Cartas).getPlayers(),userCartas.getNombre());
				Tablero tbCartas = ms.findMatch(matchId_Cartas);
				
				jso = new JSONObject();
				jso.put("type", "MATCH UPDATE");
				jso.put("player", personaActualizarTableroCartas);
				jso.put("matchId", matchId_Cartas);
				jso.put("cartas1", tbCartas.getCartas1());
				jso.put("cartas2", tbCartas.getCartas2());
				jso.put("cartasMesa", tbCartas.getCartasMesa());
				
				//Verificar ganador
				if (ms.findMatch(matchId_Cartas).getGanador() != Character.MIN_VALUE) {
					char ganador = ms.findMatch(matchId_Cartas).getGanador();
					jso.put("winner", ganador);
					List<User> jugadoresPartida = tbCartas.getPlayers();
					String nick_ganador="";
					
					if (ganador == '1') {
						nick_ganador = jugadoresPartida.get(0).getNombre();
					}else {
						nick_ganador = jugadoresPartida.get(1).getNombre();
					}
					jso.put("nickWinner", nick_ganador);
					ms.almacenarEstadistica(matchId_Cartas, "La escoba", nick_ganador, "Gana la partida");
				}
				
				ms.notificarMovimiento(matchId_Cartas, jso);
				break;
			case "WEATHER":
				String nickUser = jso.getString("user");
				String idPartida = jso.getString("idPartida");
				String cdTiempo = jso.getString("tiempo");
				
				List<User> lis = ms.findMatch(idPartida).getPlayers();
				ms.notificarMovimiento(idPartida, jso);
				break;
			//CHAT
			case "MSG":
				String nombre = jso.getString("nombre");
				String matchIdChat = jso.getString("matchId");
				String msg = jso.getString("contenido");
				Tablero game = ms.findMatch(matchIdChat);
				if (game!=null) {
					jso.put("nombre", nombre);
					jso.put("msg", msg);
					ms.difundirMsg(jso, game.getPlayers());
				}
				break;
			//Para pruebas Jmeter
			case "PRUEBA":
				jso.put("msg", "Hola");
				break;
		}
	}
	
	
	public static void send(WebSocketSession sessionWs, Object... clavesyValores) {
		JSONObject objJson = new JSONObject();
		//i+=2 Clave/valor
	    for (int i = 0; i < clavesyValores.length; i += 2) {
	    	objJson.put(clavesyValores[i].toString(), clavesyValores[i + 1]);
	    }
	    
	    WebSocketMessage<?> webSocketMessage = new TextMessage(objJson.toString());
	    
	    try {
	    	sessionWs.sendMessage(webSocketMessage);
	    } catch (IOException e) {
	        System.out.println("[Método SEND] Se ha producido una excepción:" +e.getMessage());
	    }
	}

	private void bienvenida(WebSocketSession sessionDelTipoQueAcabaDeLlegar) {
		JSONObject jso = new JSONObject();
		jso.put("tipo", "BIENVENIDA");
		
		JSONArray jsaUsuarios = new JSONArray();
		
		Collection<SesionWS> usuariosConectados = this.sessionsByNombre.values();
		for (SesionWS usuarioConectado : usuariosConectados) {
			if (usuarioConectado.getSession()!=sessionDelTipoQueAcabaDeLlegar) {
				jsaUsuarios.put(usuarioConectado.getNombre());
			}
		}
		jso.put("usuarios", jsaUsuarios);
		try {
			sessionDelTipoQueAcabaDeLlegar.sendMessage(new TextMessage(jso.toString()));
		} catch (IOException e) {
			this.eliminarSesion(sessionDelTipoQueAcabaDeLlegar);
		}
	}
	
	private void difundir(WebSocketSession remitente, Object... clavesyValores) {
		// tipo, NUEVO USUARIO, nombre, Pepe, edad, 20, curso, 4º
		JSONObject jso = new JSONObject();
		for (int i=0; i<clavesyValores.length; i=i+2) {
			String clave = clavesyValores[i].toString();
			String valor = clavesyValores[i+1].toString();
			jso.put(clave, valor);
		}
		for (WebSocketSession session : this.sessions) {
			if (session!=remitente) {
				try {
					session.sendMessage(new TextMessage(jso.toString()));
				} catch (IOException e) {
					this.eliminarSesion(session);
				}
			}
		}
	}
	
	private void eliminarSesion(WebSocketSession session) {
		this.sessions.remove(session);
		SesionWS sesionWS = this.sessionsById.remove(session.getId());
		this.sessionsByNombre.remove(sesionWS.getNombre());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//String httpSessionId = obtenerHttpId(session);
		System.out.printf("[WebSocket] La Sesión se ha cerrado correctamente\n");
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
	
	private static String obtenerSiguienteNombre(List<User> list, String nombreBuscado) {
		String nombre = "";
        if (list.get(0).getNombre().equals(nombreBuscado)){
        	nombre = list.get(1).getNombre();
        }else {
        	nombre = list.get(0).getNombre();
        }
        return nombre;
    }
}