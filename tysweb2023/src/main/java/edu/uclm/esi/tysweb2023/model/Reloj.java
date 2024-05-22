package edu.uclm.esi.tysweb2023.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.uclm.esi.tysweb2023.http.UserController;
import edu.uclm.esi.tysweb2023.services.MatchService;
import edu.uclm.esi.tysweb2023.ws.ManagerWS;
import jakarta.servlet.http.HttpSession;

public class Reloj implements Runnable {
    private final Tablero tablero;
    private final MatchService matchService;
    private final HttpSession session;
    private final ScheduledExecutorService scheduler;
    
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
	                    if (tablero.getJugadorConElTurno().getId().equals("j23jh4h5")) {
	                        String tablero_id = tablero.getId();
	                        if (this.matchService.findMatch(tablero_id).getGanador() == Character.MIN_VALUE) {
	                            int col = (int)(Math.random() * 7);
	                            Map<String, Object> movimiento = new HashMap<>();
	                            movimiento.put("col", col);
	                            System.out.println("pongo ficha en columna: "+ col);
	                            this.matchService.poner(tablero_id, movimiento, "j23jh4h5");
	                        }
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
