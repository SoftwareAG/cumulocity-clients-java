/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.measurement.autopoll;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cumulocity.sdk.client.FixedRatePoller;

/**
 * Starts a regular poller that adds measurement operation requests to the
 * operations queue at a regular interval. Note: this only adds the request -
 * processing the request must be handled by a concrete OperationProcessor.
 */
public class MeasurementFixedRatePoller extends FixedRatePoller {

    public MeasurementFixedRatePoller(long periodInterval) {
        super(new ScheduledThreadPoolExecutor(1), periodInterval);
        setPollingTask(new MeasurementRequestTask());
    }

}
