package com.cumulocity.mqtt.connect.client.exception;

public class GenericMqttClientException extends RuntimeException {

    public GenericMqttClientException(String message) {
        super(message);
    }

    public GenericMqttClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
