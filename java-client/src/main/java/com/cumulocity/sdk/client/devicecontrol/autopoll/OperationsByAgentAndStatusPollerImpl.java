/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol.autopoll;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.sdk.client.FixedRatePoller;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;

/**
 * This class is an implementation of OperationsPollerInterface, its responsibility is to poll device control REST API for
 * operations for given agent id. These operations are fetched and added to queue and there is a thread, which is polling
 * operations from this queue and process them (using Poller interface implementation).
 */
public class OperationsByAgentAndStatusPollerImpl extends FixedRatePoller {

    private static long PERIOD_INTERVAL = 10000;

    private OperationsQueueHandler queueHandler;

    public OperationsByAgentAndStatusPollerImpl(DeviceControlApi deviceControlApi, String agentId, OperationStatus status,
            OperationProcessor operationProcessor) throws SDKException {
        super(new ScheduledThreadPoolExecutor(1), PERIOD_INTERVAL);

        OperationFilter filter = new OperationFilter().byAgent(agentId).byStatus(status);
        setPollingTask(new OperationsPollingTask(deviceControlApi.getOperationsByFilter(filter), OperationsQueue.getInstance()));
        queueHandler = new OperationsQueueHandler(operationProcessor, OperationsQueue.getInstance(), deviceControlApi);
    }

    @Override
    public boolean start() {
        if (!super.start()) {
            return false;
        }

        //start operationRep handler, which is getting operations from blocking queue
        queueHandler.start();
        return true;
    }

    @Override
    public void stop() {
        super.stop();

        //stop operationRep handler, which is getting operations from blocking queue
        queueHandler.stop();
    }
}
