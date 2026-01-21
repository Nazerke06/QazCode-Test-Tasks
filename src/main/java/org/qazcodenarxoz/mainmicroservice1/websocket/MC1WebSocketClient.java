package org.qazcodenarxoz.mainmicroservice1.websocket;

import com.example.dmc1.entity.MC1Entity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class MC1WebSocketClient {

    private final ObjectMapper objectMapper;
    private final MC1WebSocketSessionHolder sessionHolder;

    @Value("${ws.uri}")
    private String WS_URI;
//    private String WS_URI="ws://192.168.152.27:8080/ws";

    public MC1WebSocketClient(
            ObjectMapper objectMapper,
            MC1WebSocketSessionHolder sessionHolder
    ) {
        this.objectMapper = objectMapper;
        this.sessionHolder = sessionHolder;
        connectWebSocket();
    }

    private void connectWebSocket() {
        TextWebSocketHandler handler = new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("✅ WebSocket connected to {}", WS_URI);
                sessionHolder.setSession(session);
            }
        };

        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                new StandardWebSocketClient(),
                handler,
                WS_URI
        );
        manager.setAutoStartup(true);
        manager.start();
    }

    public void sendMessageWhenReady(MC1Entity entity) {
        new Thread(() -> {
            while (!sessionHolder.isConnected()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            try {
                String json = objectMapper.writeValueAsString(entity);
                WebSocketSession session = sessionHolder.getSession();
                if (session != null && session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                    log.info("➡️ WS message sent: {}", json);
                }
            } catch (Exception e) {
                log.error("Failed to send WS message", e);
            }
        }).start();
    }
}
