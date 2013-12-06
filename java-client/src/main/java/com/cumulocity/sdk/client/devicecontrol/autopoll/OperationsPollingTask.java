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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.sdk.client.devicecontrol.OperationCollection;
import com.cumulocity.sdk.client.devicecontrol.PagedOperationCollectionRepresentation;

/**
 * This class is responsible for polling the given OperationCollection and putting received operations into the given queue
 */
public class OperationsPollingTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(OperationsPollingTask.class);

    OperationCollection operationCollection;

    OperationsQueue queue;

    public OperationsPollingTask(OperationCollection operationCollection, OperationsQueue queue) {
        this.operationCollection = operationCollection;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // polls for new operationRep representations and queue them
            PagedOperationCollectionRepresentation collectionReps = operationCollection.get();
            queue.addAll(collectionReps.getOperations());
        } catch (Exception e) {
            logger.error("Problem polling for operations", e);
        }
    }
}
