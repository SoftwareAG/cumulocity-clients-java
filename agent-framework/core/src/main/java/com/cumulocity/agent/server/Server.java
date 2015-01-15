package com.cumulocity.agent.server;

public interface Server {
    void start();

    void awaitTerminated();

    void stop();
}
