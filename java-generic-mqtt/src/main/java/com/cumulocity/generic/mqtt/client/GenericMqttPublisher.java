package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

/**
 * <code>GenericMqttPublisher</code> is used to publish messages to a topic.
 * <p>
 * Topic is configured when obtaining the instance of <code>GenericMqttPublisher</code> using {@link GenericMqttClient#builder()}
 */
public interface GenericMqttPublisher extends AutoCloseable {
    /**
     * Sends {@link GenericMqttMessage message} to the Generic MQTT.
     *
     * @param message The <code>GenericMqttMessage</code> which will be published.
     */
    void publish(GenericMqttMessage message);
}
