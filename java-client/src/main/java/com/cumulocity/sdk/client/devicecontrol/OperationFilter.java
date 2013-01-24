/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.model.operation.OperationStatus;

/**
 * A filter to be used in operation queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code OperationFilter filter = new OperationFilter().byStatus(status).byDevice(deviceId);}
 */
public class OperationFilter {

    private OperationStatus status;

    private String deviceId;

    private String agentId;

    /**
     * Specifies the {@code status} query parameter
     *
     * @param status status of the operation(s)
     * @return the operation filter with {@code status} set
     */
    public OperationFilter byStatus(OperationStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Specifies the {@code deviceId} query parameter
     *
     * @param deviceId id of the device associated with the the operations(s)
     * @return the operation filter with {@code deviceId} set
     */
    public OperationFilter byDevice(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    /**
     * Specifies the {@code agentId} query parameter
     *
     * @param agentId id of the agent associated with the the operations(s)
     * @return the operation filter with {@code agentId} set
     */
    public OperationFilter byAgent(String agentId) {
        this.agentId = agentId;
        return this;
    }

    /**
     * @return the {@code status} parameter of the query
     */
    public OperationStatus getStatus() {
        return status;
    }

    /**
     * @return the {@code deviceId} parameter of the query
     */
    public String getDevice() {
        return deviceId;
    }

    /**
     * @return the {@code agentId} parameter of the query
     */
    public String getAgent() {
        return agentId;
    }

}
