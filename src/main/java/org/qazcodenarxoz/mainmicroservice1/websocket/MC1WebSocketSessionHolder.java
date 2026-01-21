package org.qazcodenarxoz.mainmicroservice1.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class MC1WebSocketSessionHolder {
    private WebSocketSession session;

    public boolean isConnected(){
        return session != null && session.isOpen();
    }

}
