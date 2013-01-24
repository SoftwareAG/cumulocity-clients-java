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

package com.cumulocity.sdk.client.measurement.autopoll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsQueue;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsQueueHandler;

/**
 * Task that adds a new measurement operation request to the internal Agent operation queue
 */
public class MeasurementRequestTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MeasurementRequestTask.class);

    public OperationRepresentation getNewMeasurementOperation() {
        OperationRepresentation newOp = Operations.createNewMeasurementOperation();
        newOp.setId(new GId(OperationsQueueHandler.INTERNAL + ":NewMeasurement-" + String.valueOf(System.currentTimeMillis())));
        return newOp;
    }

    @Override
    public void run() {
        try {
            OperationsQueue.getInstance().add(getNewMeasurementOperation());
        } catch (Exception e) {
            LOG.error("Problem polling for operations", e);
        }
    }

}
