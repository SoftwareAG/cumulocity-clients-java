/*
 * Copyright 2012 Nokia Siemens Networks 
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
