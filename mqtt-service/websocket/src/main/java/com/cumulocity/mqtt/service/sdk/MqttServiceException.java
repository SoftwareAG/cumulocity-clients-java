package com.cumulocity.mqtt.service.sdk;

public class MqttServiceException extends RuntimeException {

    public MqttServiceException(String message) {
        super(message);
    }

    public MqttServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
