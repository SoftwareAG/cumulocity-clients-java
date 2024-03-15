package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.model.MqttMessage;

/**
 * Users must implement this interface and pass the instance of it to {@link MqttSubscriber#subscribe(MessageListener)}
 * in order to handle MQTT messages received from the server.
 */
@FunctionalInterface
public interface MessageListener {

    /**
     * Callback for {@link MqttMessage} messages received from the remote host. The messages will be acknowledged if no exception is raised.
     *
     * @param message The {@link MqttMessage} that was received.
     */
    void onMessage(MqttMessage message);

}
