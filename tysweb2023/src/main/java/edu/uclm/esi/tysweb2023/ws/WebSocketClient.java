package edu.uclm.esi.tysweb2023.ws;

import javax.websocket.*;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@ClientEndpoint
public class WebSocketClient {
    private Session session;
    private CompletableFuture<Session> connectFuture = new CompletableFuture<>();

    public CompletableFuture<Session> connect(String uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (Exception e) {
            connectFuture.completeExceptionally(e);
        }
        return connectFuture;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to WebSocket server");
        connectFuture.complete(session);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        // Handle incoming messages from the server here
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Connection closed: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        connectFuture.completeExceptionally(throwable);
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }
}

