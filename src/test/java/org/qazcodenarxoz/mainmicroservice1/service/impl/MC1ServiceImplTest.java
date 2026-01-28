package org.qazcodenarxoz.mainmicroservice1.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.qazcodenarxoz.mainmicroservice1.repository.MC1Repository;
import org.qazcodenarxoz.mainmicroservice1.websocket.MC1WebSocketClient;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MC1ServiceImplTest {

    @Mock
    private MC1WebSocketClient mc1WebSocketClient;

    @Mock
    private MC1Repository mc1Repository;

    @InjectMocks
    private MC1ServiceImpl mc1Service;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(mc1Service, "DURATION_SECONDS", 10L);
    }

    @Test
    void testStart_Success() {

        String result = mc1Service.start();

        assertEquals("Started", result);
        assertTrue(mc1Service.isRunning());
        assertEquals(1, mc1Service.getMessageCount().get());

        verify(mc1WebSocketClient, times(1)).connectWebSocket();

        verify(mc1WebSocketClient, times(1)).sendMessageWhenReady(any(MC1Entity.class));
    }

    @Test
    void testStart_WhenAlreadyRunning() {

        mc1Service.start();

        String result = mc1Service.start();

        assertEquals("Already running", result);

        verify(mc1WebSocketClient, times(1)).connectWebSocket();
    }

    @Test
    void testCycle_WhenRunning_ShouldSaveAndSendNew() {

        mc1Service.start();
        MC1Entity incomingEntity = new MC1Entity();
        incomingEntity.setSessionId(1);

        clearInvocations(mc1WebSocketClient);

        String result = mc1Service.cycle(incomingEntity);

        assertEquals("Cycle continued", result);

        ArgumentCaptor<MC1Entity> entityCaptor = ArgumentCaptor.forClass(MC1Entity.class);
        verify(mc1Repository).save(entityCaptor.capture());
        MC1Entity savedEntity = entityCaptor.getValue();
        assertNotNull(savedEntity.getEndTimestamp(), "End timestamp должен быть установлен");

        verify(mc1WebSocketClient, times(1)).sendMessageWhenReady(any(MC1Entity.class));

        assertEquals(2, mc1Service.getMessageCount().get());
    }

    @Test
    void testCycle_WhenNotRunning_ShouldStop() {

        MC1Entity incomingEntity = new MC1Entity();

        String result = mc1Service.cycle(incomingEntity);

        assertEquals("Stopped", result);

        verify(mc1Repository, never()).save(any());
        verify(mc1WebSocketClient, never()).sendMessageWhenReady(any());
    }

    @Test
    void testStop_WhenRunning() {
        mc1Service.start();
        assertTrue(mc1Service.isRunning());

        String result = mc1Service.stop();
        assertEquals("Stopped", result);
        assertFalse(mc1Service.isRunning());
    }

    @Test
    void testStop_WhenNotRunning() {
        String result = mc1Service.stop();

        assertEquals("Interaction is not running", result);
    }
}