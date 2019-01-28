package com.cumulocity.sdk.paho.exception;

public class PahoDeviceSDKException extends RuntimeException {

    private static final long serialVersionUID = 3692806955312308633L;

    public PahoDeviceSDKException(String message) {
        super(message);
    }

    public PahoDeviceSDKException(String message, Throwable cause) {
        super(message, cause);
    }
}
