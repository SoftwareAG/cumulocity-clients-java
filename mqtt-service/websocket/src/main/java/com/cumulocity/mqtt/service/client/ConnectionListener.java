package com.cumulocity.mqtt.service.client;

/**
 * Interface for listening to connection for both {@link MqttPublisher} and {@link MqttSubscriber}.
 */
public interface ConnectionListener {

    /**
     * Called when errors occurs.
     * <br> This method will be called primarily because of IO or protocol errors.
     * <br> If the given exception is an RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param error    The {@link Throwable} class that is causing this error.
     * @param sourceId The {@link MqttSubscriber} or {@link MqttPublisher} id.
     */
    void onError(Throwable error, String sourceId);

    /**
     * Called when the client is disconnected from the server.
     *
     * @param reason   human-readable disconnect reason.
     * @param sourceId The {@link MqttSubscriber} or {@link MqttPublisher} id.
     */
    void onDisconnected(String reason, String sourceId);

}
