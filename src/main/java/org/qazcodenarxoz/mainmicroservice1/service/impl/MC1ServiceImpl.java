package org.qazcodenarxoz.mainmicroservice1.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.mainmicroservice1.websocket.MC1WebSocketClient;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.qazcodenarxoz.mainmicroservice1.service.MC1Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MC1ServiceImpl implements MC1Service {

    private final MC1WebSocketClient mc1WebSocketClient;
    private boolean running = false;
    private long startTime;
    private long messageCount;
    private ScheduledExecutorService scheduler  = Executors.newSingleThreadScheduledExecutor();

    @Value("${app.timer.duration-seconds}")
    private Long DURATION_SECONDS;


    @Override
    public synchronized String start() {
        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            MC1Entity entity = new MC1Entity();
            entity.setMc1Timestamp(Instant.now());
            try {
                mc1WebSocketClient.sendMessageWhenReady(entity);
                messageCount++;
            } catch (Exception e) {
                log.error("Failed to send WS message", e);
            }
        }, 0, 1, TimeUnit.SECONDS);
        running = true;
        startTime = System.currentTimeMillis();
        messageCount = 0;
        scheduler.schedule(this::stop, DURATION_SECONDS, TimeUnit.SECONDS);

        try {
            mc1WebSocketClient.sendMessageWhenReady(new MC1Entity());
            messageCount++;
        } catch (Exception e) {
            log.error("Failed to send WS message", e);
        }

        log.info("Interaction started");
        return "Started";
    }

    @Override
    public String cycle(MC1Entity entity){
        if (!running){
            start();
        }
        return "Interaction ended";
    }



    @Override
    public synchronized String stop() {
        if (!running) {
            return "Interaction is not running";
        }
        running = false;

//        if (scheduler != null) {
//            scheduler.shutdown();
//        }
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
}