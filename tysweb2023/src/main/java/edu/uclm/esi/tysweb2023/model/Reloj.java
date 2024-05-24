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
	                			int col = elegirColumnaParaGanar(tablero.mostrarCasillas());
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
        					
        					if (ganador == 65) {
        						nick_ganador = jugadoresPartida.get(1).getNombre();
        					}else {
        						nick_ganador = jugadoresPartida.get(0).getNombre();
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
    
    private int elegirColumnaParaGanar(JSONArray casillas) {
        int rows = casillas.length();
        int cols = casillas.getJSONArray(0).length();
        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = casillas.getJSONArray(i).getString(j).charAt(0);
            }
        }

        // Intentar encontrar una columna para ganar (prioridad máxima)
        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row >= 0; row--) {
                if (board[row][col] == '\u0000') {
                    board[row][col] = 'A';
                    if (esGanador(board, row, col, 'A')) {
                        return col;
                    }
                    board[row][col] = '\u0000';
                    break;
                }
            }
        }

        // Intentar bloquear al oponente (prioridad secundaria)
        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row >= 0; row--) {
                if (board[row][col] == '\u0000') {
                    board[row][col] = 'R';
                    if (esGanador(board, row, col, 'R')) {
                        board[row][col] = '\u0000'; // Deshacer la jugada de prueba
                        return col;
                    }
                    board[row][col] = '\u0000';
                    break;
                }
            }
        }

        // Si no hay una jugada ganadora ni de bloqueo, elegir la primera columna disponible
        for (int col = 0; col < cols; col++) {
            for (int row = rows - 1; row >= 0; row--) {
                if (board[row][col] == '\u0000') {
                    return col;
                }
            }
        }

        // En caso de que todas las columnas estén llenas
        return 0;
    }

    private boolean esGanador(char[][] board, int row, int col, char player) {
        // Comprobación horizontal
        if (checkDirection(board, row, col, player, 1, 0) + checkDirection(board, row, col, player, -1, 0) >= 3)
            return true;

        // Comprobación vertical
        if (checkDirection(board, row, col, player, 0, 1) + checkDirection(board, row, col, player, 0, -1) >= 3)
            return true;

        // Comprobación diagonal (de abajo-izquierda a arriba-derecha)
        if (checkDirection(board, row, col, player, 1, 1) + checkDirection(board, row, col, player, -1, -1) >= 3)
            return true;

        // Comprobación diagonal (de arriba-izquierda a abajo-derecha)
        if (checkDirection(board, row, col, player, 1, -1) + checkDirection(board, row, col, player, -1, 1) >= 3)
            return true;

        return false;
    }

    private int checkDirection(char[][] board, int row, int col, char player, int deltaRow, int deltaCol) {
        int count = 0;
        int r = row + deltaRow;
        int c = col + deltaCol;
        while (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == player) {
            count++;
            r += deltaRow;
            c += deltaCol;
        }
        return count;
    }
    
}
