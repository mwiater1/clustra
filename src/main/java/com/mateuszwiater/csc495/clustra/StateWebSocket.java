package com.mateuszwiater.csc495.clustra;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class StateWebSocket {
    private final Queue<Session> sessions;
    private final ClusterNode clusterNode;
    private final Gson gson;

    public StateWebSocket(final ClusterNode clusterNode) {
        this.sessions = new ConcurrentLinkedQueue<>();
        this.clusterNode = clusterNode;
        this.gson = new Gson();

        clusterNode.addStateUpdateListener(state -> sessions.forEach(session -> {
            try {
                session.getRemote().sendString(gson.toJson(state));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        try {
            session.getRemote().sendString(gson.toJson(clusterNode.getState()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) {
        clusterNode.send(gson.fromJson(message, State.class));
    }
}
