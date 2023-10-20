package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.model.MqttMessage;

/**
 * Users must implement this interface and pass the instance of it to {@link MqttSubscriber#subscribe(MqttMessageListener)}
 * in order to handle MQTT messages received from the server.
 */
public interface MqttMessageListener {

    /**
     * Callback for {@link MqttMessage} messages received from the remote host. The messages will be acknowledged if no exception is raised.
     *
     * @param message The {@link MqttMessage} that was received.
     */
    void onMessage(MqttMessage message);

    /**
     * Called when errors occurs.
     * <br> This method will be called primarily because of IO or protocol errors.
     * <br> If the given exception is an RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param t The {@code Throwable} class that is causing this error.
     */
    void onError(Throwable t);

}
