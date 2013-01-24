/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol.autopoll;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

/**
 * This is callback interface for processing new operations. This callback should provide business logic only, update of
 * operation status should be handled by the platform.
 */
public interface OperationProcessor {
    /**
     * Processes operation
     *
     * @param operation operation to process
     * @return true - if processing was successful, false - otherwise
     */
    boolean process(OperationRepresentation operation);

}
