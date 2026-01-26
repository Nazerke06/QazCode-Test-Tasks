package org.qazcodenarxoz.mainmicroservice1.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MC1WebSocketClient {

    private final ObjectMapper objectMapper;
    private final MC1WebSocketSessionHolder sessionHolder;

    @Value("${ws.uri}")
    private String WS_URI;

    public void connectWebSocket() {
        TextWebSocketHandler handler = new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
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
        sessionHolder.onConnected()
                .thenAccept(session -> {
                    try {
                        if (session.isOpen()) {
                            String json = objectMapper.writeValueAsString(entity);
                            session.sendMessage(new TextMessage(json));
                            log.info("➡️ WS message sent: {}", json);
                        }
                    } catch (Exception e) {
                        log.error("Failed to send WS message", e);
                    }
                })
                .exceptionally(ex -> {
                    log.error("WS connection failed", ex);
                    return null;
                });
    }
}
