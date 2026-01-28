package org.qazcodenarxoz.mainmicroservice1.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class MC1WebSocketSessionHolder {
    private WebSocketSession session;
    private final CompletableFuture<WebSocketSession> connectedFuture =
            new CompletableFuture<>();

    public boolean isConnected(){
        return session != null && session.isOpen();
    }

    public CompletableFuture<WebSocketSession> onConnected() {
        return connectedFuture;
    }
    public synchronized void setSession(WebSocketSession session) {
        this.session = session;
        if (!connectedFuture.isDone()) {
            connectedFuture.complete(session);
        }
    }
}
