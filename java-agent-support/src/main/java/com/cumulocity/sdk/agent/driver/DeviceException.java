/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.driver;

/**
 * Represents a device error. Can be i.e. due to failure in executing device operationRep.
 */
public class DeviceException extends Exception {

    private static final long serialVersionUID = -5532961687086801387L;

    /**
     * @param message the reason of device failure.
     */
    public DeviceException(String message) {
        super(message);
    }
    
    /**
     * @param message the reason of device failure.
     * @param cause the root cause of failure.
     */
    public DeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}
