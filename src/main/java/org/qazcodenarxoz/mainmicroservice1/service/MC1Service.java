package org.qazcodenarxoz.mainmicroservice1.service;

import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;

import java.util.concurrent.atomic.AtomicLong;

public interface MC1Service {

    String start();
    String stop();
    boolean isRunning();
    AtomicLong getMessageCount();
    String cycle(MC1Entity entity);
}
