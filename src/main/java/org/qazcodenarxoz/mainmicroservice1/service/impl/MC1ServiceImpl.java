package org.qazcodenarxoz.mainmicroservice1.service.impl;

import com.example.dmc1.entity.MC1Entity;
import com.example.dmc1.service.MC1Service;
import com.example.dmc1.websocket.MC1WebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class MC1ServiceImpl implements MC1Service {

    private final MC1WebSocketClient mc1WebSocketClient;
    private boolean running = false;
    private long startTime;
    private long messageCount;

    public MC1ServiceImpl(MC1WebSocketClient mc1WebSocketClient) {
        this.mc1WebSocketClient = mc1WebSocketClient;
    }

    @Override
    public synchronized String start() {
        if (running) {
            return "Interaction already started";
        }
        running = true;
        startTime = System.currentTimeMillis();
        messageCount = 0;

        try {
            mc1WebSocketClient.sendMessageWhenReady(new MC1Entity(1, 0, Instant.now(), null, null, null));
            incrementMessages();
        } catch (Exception e) {
            log.error("Failed to send WS message", e);
        }

        log.info("Interaction started");
        return "Started";
    }


    @Override
    public synchronized String stop() {
        if (!running) {
            return "Interaction is not running";
        }
        running = false;

        long duration = (System.currentTimeMillis() - startTime) / 1000;

        log.info("Interaction finished");
        log.info("Duration (sec): " + duration);
        log.info("Messages count: " + messageCount);

        return "Stopped";
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public long getMessageCount() {
        return messageCount;
    }

    public void incrementMessages() {
        messageCount++;
    }
}
