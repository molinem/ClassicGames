package edu.uclm.esi.tysweb2023.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.tysweb2023.http.UserController;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.ws.ManagerWS;
import jakarta.servlet.http.HttpSession;

public class Reloj implements Runnable {
    private final Tablero tablero;
    private final MatchService matchService;
    private final HttpSession session;
    private final ScheduledExecutorService scheduler;
    
    private String tablero_id;
    private JSONObject jso;
    
    public Reloj(Tablero tablero, MatchService matchService, HttpSession session) {
        this.tablero = tablero;
        this.matchService = matchService;
        this.session = session;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            tablero_id = tablero.getId();
            
            if (!tablero.checkPartidaLista()) {
                Robot robot = new Robot();
                session.getAttribute("user");

                UserController.httpSessions.put(session.getId(), session);
                ManagerWS.get().addSessionByUserId(robot.getId(), session);

                this.matchService.newMatch(robot,"Tablero4R");
                this.matchService.notificarEstado("START", tablero.getId());
            }
            
	     
	        scheduler.scheduleAtFixedRate(() -> {
	            try {
	                if (tablero.checkPartidaLista()) {
	                	//No hay ganador
	                	if (this.matchService.findMatch(tablero_id).getGanador() == Character.MIN_VALUE) {
	                		if (tablero.getJugadorConElTurno().getId().equals("j23jh4h5")) {
	                			int col = (int)(Math.random() * 7);
	                            Map<String, Object> movimiento = new HashMap<>();
	                            movimiento.put("col", col);
	                            Tablero tbUpdate = this.matchService.poner(tablero_id, movimiento, "j23jh4h5");
	                            
	                            JSONArray casillas = tbUpdate.mostrarCasillas();
	                            jso = new JSONObject();
	            				jso.put("type", "MATCH UPDATE");
	            				jso.put("player", tablero.getPlayers().get(0).getNombre().toString());
	            				jso.put("matchId", tablero_id);
	            				jso.put("board", casillas);
	                            
	                            this.matchService.notificarMovimiento(tablero_id, jso);
	                		}
	                	}else {
	                		//Si hay ganador
	                		jso = new JSONObject();
	                		
        					char ganador = this.matchService.findMatch(tablero_id).getGanador();
        					jso.put("winner", ganador);
        					List<User> jugadoresPartida = tablero.getPlayers();
        					String nick_ganador="";
        					
        					if (ganador == 'R') {
        						nick_ganador = jugadoresPartida.get(0).getNombre();
        					}else {
        						nick_ganador = jugadoresPartida.get(1).getNombre();
        					}
        					jso.put("nickWinner", nick_ganador);
	        				
	                		this.matchService.notificarMovimiento(tablero_id, jso);
	                		scheduler.shutdown();
	                	}
	                	
	                    
	                }
	                
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }, 0, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
