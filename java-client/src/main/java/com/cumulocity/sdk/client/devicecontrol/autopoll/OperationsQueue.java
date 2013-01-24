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

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

/**
 * This queue is handling operations to be processed. It's based on LinkedBlockingQueue implementation, but
 * it assures that all operations are unique (unique means all operations in queue have unique ids). Every
 * time new operation is added to queue, it is checked that operation with the same id doesn't exists in queue
 * (if it does, new item is not queued).
 */
public class OperationsQueue extends LinkedBlockingQueue<OperationRepresentation> {
    private static final long serialVersionUID = -2987475330088840639L;

    private static OperationsQueue instance = null;

    // Singleton access - change if moved to Spring
    public static OperationsQueue getInstance() {
        if (instance == null) {
            instance = new OperationsQueue();
        }
        return instance;
    }

    @Override
    /**
     * Adds operation only if it's not existing in queue
     */
    public boolean add(OperationRepresentation arg0) {
        if (contains(arg0)) {
            return false;
        }
        return super.add(arg0);
    }

    @Override
    /**
     * Adds operations to queue, but only ones which are not existing in queue already
     */
    public boolean addAll(Collection<? extends OperationRepresentation> arg0) {
        boolean result = false;
        for (OperationRepresentation operation : arg0) {
            result = result | add(operation);
        }
        return result;
    }

    @Override
    public boolean contains(Object arg0) {
        //if not instance of OperationRepresentation then not contains
        if (!(arg0 instanceof OperationRepresentation)) {
            return false;
        }
        OperationRepresentation operation = (OperationRepresentation) arg0;

        //iterate over all elements in queue and compare theirs ids
        Iterator<OperationRepresentation> iterator = iterator();
        while (iterator.hasNext()) {
            OperationRepresentation current = iterator.next();
            //if element in list have the same id, we know list contains it
            if (current != null && current.getId() != null && current.getId().equals(operation.getId())) {
                return true;
            }
        }

        //if no match, then list doesn't contain that element
        return false;
    }
}
