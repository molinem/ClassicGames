package edu.uclm.esi.tysweb2023.ws;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.tysweb2023.dao.UserDAO;
import jakarta.servlet.http.HttpSession;

/**
 * Gestiona 
 */
@Component
public class ManagerWS {
	@Autowired
	private UserDAO userDAO;
	
	private ConcurrentHashMap<String, SesionWS> sessionsByUserId;
	private ConcurrentHashMap<String, SesionWS> sessionsByHttpId;
	private ConcurrentHashMap<String, SesionWS> sessionsByWsId;
	//private ConcurrentHashMap<String, Match> matches;
	
	private ManagerWS() {
		this.sessionsByUserId = new ConcurrentHashMap<>();
		this.sessionsByHttpId = new ConcurrentHashMap<>();
		this.sessionsByWsId = new ConcurrentHashMap<>();
		//this.matches = new ConcurrentHashMap<>();
	}
	
	private static class ManagerHolder {
		static ManagerWS singleton = new ManagerWS();
	}
	
	@Bean
	public static ManagerWS get() {
		return ManagerHolder.singleton;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public void addSessionByUserId(String userId, HttpSession httpSession) {
		SesionWS hwSession = new SesionWS(httpSession);
		this.sessionsByUserId.put(userId, hwSession);
		this.sessionsByHttpId.put(httpSession.getId(), hwSession);
	}
	
	public void setWebsocketSession(String httpSessionId, WebSocketSession websocketSession) {
		SesionWS hwSession = this.sessionsByHttpId.get(httpSessionId);
		hwSession.setWebsocketSession(websocketSession);
		this.sessionsByWsId.put(websocketSession.getId(), hwSession);
	}
	
	public SesionWS getSessionByUserId(String userId) {
		return this.sessionsByUserId.get(userId);
	}
	
	public SesionWS removeSessionByUserId(String userId) {
		SesionWS hwSession = this.sessionsByUserId.remove(userId);
		this.sessionsByHttpId.remove(hwSession.getHttpSession().getId());
		if (hwSession.getWebsocketSession()!=null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}
	
	public SesionWS getSessionByHttpId(String httpId) {
		return this.sessionsByHttpId.get(httpId);
	}
	
	public SesionWS getSessionByWebSocketId(String wsId) {
		return this.sessionsByWsId.get(wsId);
	}
	
	public SesionWS removeSessionByHttpId(String httpId) {
		SesionWS hwSession = this.sessionsByHttpId.remove(httpId);
		this.sessionsByUserId.remove(hwSession.getUserId());
		if (hwSession.getWebsocketSession()!=null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}
	
	public void invalidate(SesionWS existingSession) {
		existingSession.getHttpSession().invalidate();
		try {
			existingSession.getWebsocketSession().close();
		} catch (IOException e) { 
			this.removeSessionByHttpId(existingSession.getHttpSession().getId());
		}
	}
	
	/*
	public void add(Match match) {
		this.matches.put(match.getId(), match);
	}
	
	public Match getMatch(String matchId) {
		return this.matches.get(matchId);
	}
	
	public Match removeMatch(String matchId) {
		return this.matches.remove(matchId);
	}*/
	
	
}
