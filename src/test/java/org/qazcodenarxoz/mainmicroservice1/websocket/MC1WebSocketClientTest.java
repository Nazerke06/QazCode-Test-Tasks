package org.qazcodenarxoz.mainmicroservice1.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MC1WebSocketClientTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MC1WebSocketSessionHolder sessionHolder;

    @Mock
    private WebSocketSession webSocketSession;

    @InjectMocks
    private MC1WebSocketClient webSocketClient;


    @Test
    void sendMessageWhenReady_shouldSendMessageIfSessionOpen() throws Exception {
        MC1Entity entity = new MC1Entity();
        String json = "{\"test\":\"ok\"}";

        when(sessionHolder.onConnected())
                .thenReturn(CompletableFuture.completedFuture(webSocketSession));

        when(webSocketSession.isOpen()).thenReturn(true);
        when(objectMapper.writeValueAsString(entity)).thenReturn(json);

        webSocketClient.sendMessageWhenReady(entity);

        verify(objectMapper).writeValueAsString(entity);
        verify(webSocketSession).sendMessage(argThat(msg ->
                ((TextMessage) msg).getPayload().equals(json)
        ));
    }

    @Test
    void sendMessageWhenReady_shouldNotSendIfSessionClosed() throws IOException {

        MC1Entity entity = new MC1Entity();

        when(sessionHolder.onConnected())
                .thenReturn(CompletableFuture.completedFuture(webSocketSession));

        when(webSocketSession.isOpen()).thenReturn(false);

        webSocketClient.sendMessageWhenReady(entity);

        verify(webSocketSession, never()).sendMessage(any());
    }

    @Test
    void sendMessageWhenReady_shouldHandleJsonException() throws Exception {

        MC1Entity entity = new MC1Entity();

        when(sessionHolder.onConnected())
                .thenReturn(CompletableFuture.completedFuture(webSocketSession));

        when(webSocketSession.isOpen()).thenReturn(true);
        when(objectMapper.writeValueAsString(entity))
                .thenThrow(new RuntimeException("JSON error"));

        webSocketClient.sendMessageWhenReady(entity);

        verify(webSocketSession, never()).sendMessage(any());
    }

    @Test
    void sendMessageWhenReady_shouldHandleConnectionFailure() {

        MC1Entity entity = new MC1Entity();

        CompletableFuture<WebSocketSession> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("WS failed"));

        when(sessionHolder.onConnected()).thenReturn(failedFuture);

        webSocketClient.sendMessageWhenReady(entity);

        verifyNoInteractions(webSocketSession);
    }
}
