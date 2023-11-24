package edu.uclm.esi.tysweb2023.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//WebSocket para chat
		registry.
		addHandler(new WSGames(), "/wsGames").
		setAllowedOrigins("*").
		addInterceptors(new HttpSessionHandshakeInterceptor());
		
		//WebSocket para tablero
		registry.
		addHandler(new WSTablero(), "/wsTablero").
		setAllowedOrigins("*").
		addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}

