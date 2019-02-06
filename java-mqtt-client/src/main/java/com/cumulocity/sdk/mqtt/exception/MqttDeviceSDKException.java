package com.cumulocity.sdk.mqtt.exception;

public class MqttDeviceSDKException extends RuntimeException {

    private static final long serialVersionUID = 3692806955312308633L;

    public MqttDeviceSDKException(String message) {
        super(message);
    }

    public MqttDeviceSDKException(String message, Throwable cause) {
        super(message, cause);
    }
}
