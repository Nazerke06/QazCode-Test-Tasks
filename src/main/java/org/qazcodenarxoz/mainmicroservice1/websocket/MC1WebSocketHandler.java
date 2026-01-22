package org.qazcodenarxoz.mainmicroservice1.websocket;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class MC1WebSocketHandler extends TextWebSocketHandler {
    private final MC1WebSocketSessionHolder sessionHolder;

    public MC1WebSocketHandler(MC1WebSocketSessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessionHolder.setSession(session);
        log.info("Connected to {}", session.getUri());
    }
}
