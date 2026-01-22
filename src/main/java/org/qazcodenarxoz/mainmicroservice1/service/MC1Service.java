package org.qazcodenarxoz.mainmicroservice1.service;

import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;

public interface MC1Service {

    String start();
    String stop();
    boolean isRunning();
    long getMessageCount();
    String cycle(MC1Entity entity);
}
