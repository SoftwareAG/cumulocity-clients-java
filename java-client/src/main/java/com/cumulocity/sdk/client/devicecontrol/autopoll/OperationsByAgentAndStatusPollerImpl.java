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
