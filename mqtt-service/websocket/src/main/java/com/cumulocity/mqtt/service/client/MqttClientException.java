package com.cumulocity.mqtt.service.client;

public class MqttClientException extends RuntimeException {

    public MqttClientException(String message) {
        super(message);
    }

    public MqttClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
