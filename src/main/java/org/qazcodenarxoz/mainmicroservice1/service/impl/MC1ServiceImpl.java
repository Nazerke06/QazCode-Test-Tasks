package org.qazcodenarxoz.mainmicroservice1.service.impl;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qazcodenarxoz.mainmicroservice1.repository.MC1Repository;
import org.qazcodenarxoz.mainmicroservice1.websocket.MC1WebSocketClient;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.qazcodenarxoz.mainmicroservice1.service.MC1Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class MC1ServiceImpl implements MC1Service {

    private final MC1WebSocketClient mc1WebSocketClient;
    private final MC1Repository mc1Repository;

    private volatile boolean running = false;
    private long startTime;
    private final AtomicLong messageCount = new AtomicLong(0);
    private final AtomicLong currentSessionId = new AtomicLong(0);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledTask;

    @Value("${app.timer.duration-seconds}")
    private Long DURATION_SECONDS;

    @Override
    public  String start() {
        if (running) return "Already running";

        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(true);
        }

        mc1WebSocketClient.connectWebSocket();
        messageCount.set(0);
        currentSessionId.incrementAndGet();
        startTime = System.currentTimeMillis();
        running = true;

        MC1Entity firstEntity = new MC1Entity();
        firstEntity.setSessionId((int) currentSessionId.get());
        firstEntity.setMc1Timestamp(Instant.now());

        sendInternal(firstEntity);

        scheduledTask = scheduler.schedule(this::stop, DURATION_SECONDS, TimeUnit.SECONDS);

        log.info("Interaction started. Session: {}", currentSessionId.get());
        return "Started";
    }

    @Override
    public String cycle(MC1Entity entity) {
        if (!running) {
            log.info("Message received but system is stopped. Saving last record.");
            return "Stopped";
        }
        finalizeAndSave(entity);

        MC1Entity nextEntity = new MC1Entity();
        nextEntity.setSessionId(entity.getSessionId());
        nextEntity.setMc1Timestamp(Instant.now());
        sendInternal(nextEntity);

        return "Cycle continued";

    }

    private void finalizeAndSave(MC1Entity entity) {
        entity.setEndTimestamp(Instant.now());
        mc1Repository.save(entity);
        log.info("📥 Message cycled and saved. ID: {}", entity);
    }

    public void sendInternal(MC1Entity entity) {
        try {
            mc1WebSocketClient.sendMessageWhenReady(entity);
            messageCount.incrementAndGet();
        } catch (Exception e) {
            log.error("Failed to send WS message", e);
        }
    }

    @Override
    public  String stop() {
        if (!running) return "Interaction is not running";

        running = false;

        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }

        long duration = (System.currentTimeMillis() - startTime) / 1000;
        log.info("=== Interaction finished ===");
        log.info("Duration: {}s", duration);
        log.info("Total messages generated: {}", messageCount.get());

        return "Stopped";
    }

    @Override
    public boolean isRunning() { return running; }

    @Override
    public AtomicLong getMessageCount() { return messageCount; }

    @PreDestroy
    public void cleanUp() {
        scheduler.shutdown();
    }
}