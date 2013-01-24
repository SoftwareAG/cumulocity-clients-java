/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.driver;

import java.util.List;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.agent.model.Device;

/**
 * A {@link Device} driver that obtains measurements from device and executes an action on a device.
 *
 * @param <D> type of devices managed by the agent.
 */
public interface DeviceDriver<D extends Device> {

    /**
     * Loads the measurements from the device.
     *
     * @param device the MPS device.
     * @return list of retrieved operations.
     * @throws DeviceException in case of device error.
     */
    List<MeasurementRepresentation> loadMeasuremntsFromDevice(D device) throws DeviceException;

    /**
     * Checks if operationRep is supported by the devices controlled by the agent.
     *
     * @param operation the operationRep to check.
     * @return <code>true</code> if operationRep is supported.
     */
    boolean isOperationSupported(OperationRepresentation operation);

    /**
     * Processes the valid operationRep execution.
     *
     * @param operation the operationRep to execute.
     * @throws DeviceException in case of failed operationRep execution.
     */
    void handleSupportedOperation(OperationRepresentation operation) throws DeviceException;

}