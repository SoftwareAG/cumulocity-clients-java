package com.cumulocity.generic.mqtt.client;

/**
 * {@link GenericMqttFactory} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttFactory {

    /**
     * Creates an instance of <code>GenericMqttPublisher</code> with the configured topic.
     *
     * @param config
     * @return the instance of <code>GenericMqttPublisher</code>
     */
    GenericMqttPublisher buildPublisher(GenericMqttConnectionConfig config);

    /**
     * Creates an instance of <code>GenericMqttSubscriber</code> with the configured topic.
     *
     * @param config
     * @return the instance of <code>GenericMqttSubscriber</code>
     */
    GenericMqttSubscriber buildSubscriber(GenericMqttConnectionConfig config);
}
