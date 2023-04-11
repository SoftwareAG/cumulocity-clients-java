package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

/**
 * Users must implement this interface and pass the instance of it to {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
 * in order to handle Generic MQTT messages received from the server.
 */
public interface GenericMqttMessageListener {

    /**
     * Callback for GenericMqttMessage messages received from the remote host. The messages will be acknowledged if no exception is raised.
     *
     * @param message The {@link GenericMqttMessage} that was received.
     */
    void onMessage(GenericMqttMessage message);

    /**
     * Called when errors occurs.
     * <br> This method will be called primarily because of IO or protocol errors.
     * <br> If the given exception is an RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param t The {@code Throwable} class that is causing this error.
     */
    void onError(Throwable t);

}
