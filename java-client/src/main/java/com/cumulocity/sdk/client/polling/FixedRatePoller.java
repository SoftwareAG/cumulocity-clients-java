/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.sdk.client.polling;

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
