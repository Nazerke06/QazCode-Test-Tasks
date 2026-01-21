package org.qazcodenarxoz.mainmicroservice1.service;

public interface MC1Service {

    String start();
    String stop();
    boolean isRunning();
    long getMessageCount();
}
