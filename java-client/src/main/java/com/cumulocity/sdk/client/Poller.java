/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

/**
 * This interface provides methods for controlling polling tasks. It can be used for any task that needs
 * to be run according to some schedule.
 */
public interface Poller {
    /**
     * Starts poller
     *
     * @return true if the poller was successfully started
     */
    boolean start();

    /**
     * Stops poller after finishing all started jobs
     */
    void stop();
}
