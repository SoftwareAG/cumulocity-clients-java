/*
 * Copyright 2012 Nokia Siemens Networks 
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
