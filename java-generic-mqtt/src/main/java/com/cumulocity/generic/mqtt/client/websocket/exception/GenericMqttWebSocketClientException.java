package com.cumulocity.generic.mqtt.client.websocket.exception;

public class GenericMqttWebSocketClientException extends RuntimeException {

    public GenericMqttWebSocketClientException() {
    }

    public GenericMqttWebSocketClientException(String message) {
        super(message);
    }

    public GenericMqttWebSocketClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericMqttWebSocketClientException(Throwable cause) {
        super(cause);
    }
}
