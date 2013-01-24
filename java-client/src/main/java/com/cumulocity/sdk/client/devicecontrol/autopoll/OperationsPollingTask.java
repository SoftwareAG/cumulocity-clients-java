/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol.autopoll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

/**
 * This class is responsible for polling the given OperationCollection and putting received operations into the given queue
 */
public class OperationsPollingTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(OperationsPollingTask.class);

    PagedCollectionResource<OperationCollectionRepresentation> operationCollection;

    OperationsQueue queue;

    public OperationsPollingTask(PagedCollectionResource<OperationCollectionRepresentation> operationCollection, OperationsQueue queue) {
        this.operationCollection = operationCollection;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // polls for new operationRep representations and queue them
            OperationCollectionRepresentation collectionReps = operationCollection.get();
            queue.addAll(collectionReps.getOperations());
        } catch (Exception e) {
            logger.error("Problem polling for operations", e);
        }
    }
}
