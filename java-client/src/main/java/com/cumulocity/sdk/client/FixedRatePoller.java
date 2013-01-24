/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A poller that triggers tasks to execute at a fixed rate.<br>
 * This class will trigger the defined pollingTask to run at the configured period.
 */
public abstract class FixedRatePoller implements Poller {
    private static final Logger LOG = LoggerFactory.getLogger(FixedRatePoller.class);

    private ScheduledThreadPoolExecutor pollingExecutor;

    private Runnable pollingTask;

    private long periodInterval;

    /**
     * Create a fixed rate poller using the given thread pool and with the given fixed rate period.
     *
     * @param periodInterval polling interval in milliseconds
     */
    public FixedRatePoller(ScheduledThreadPoolExecutor pollingExecutor, long periodInterval) {
        super();
        this.pollingExecutor = pollingExecutor;
        this.periodInterval = periodInterval;
    }

    @Override
    public boolean start() {
        if (pollingTask == null) {
            LOG.error("Poller start requested without pollingTask being set");
            return false;
        }

        //start scheduled periodic polling for new operations (only one task in scheduler at any given time)
        if (pollingExecutor.getTaskCount() == 0) {
            pollingExecutor.scheduleAtFixedRate(pollingTask, 0, periodInterval, TimeUnit.MILLISECONDS);
        }

        return true;
    }

    @Override
    public void stop() {
        //shutdown operationsPollingExecutor if it's running or if it's no shutting down just now
        pollingExecutor.shutdown();
    }

    protected void setPollingTask(Runnable pollingTask) {
        this.pollingTask = pollingTask;
    }

    protected Runnable getPollingTask() {
        return pollingTask;
    }

    /**
     * @return the fixed rate polling interval in milliseconds
     */
    protected long getPeriod() {
        return periodInterval;
    }

}
