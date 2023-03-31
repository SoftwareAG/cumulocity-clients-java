package com.cumulocity.generic.mqtt.client;

/**
 * {@link GenericMqttClient} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttClient extends AutoCloseable {

    /**
     * Creates a builder for {@link GenericMqttClient}.
     *
     * @return the created builder
     */
    static GenericMqttClientBuilder builder() {
        return new GenericMqttClientBuilder();
    }

    /**
     * Creates an instance of <code>GenericMqttPublisher</code> with the configured topic.
     *
     * @param config <code>GenericMqttConfig</code>
     * @return the instance of <code>GenericMqttPublisher</code>
     */
    GenericMqttPublisher buildPublisher(GenericMqttConfig config);

    /**
     * Creates an instance of <code>GenericMqttSubscriber</code> with the configured topic.
     *
     * @param config <code>GenericMqttConfig</code>
     * @return the instance of <code>GenericMqttSubscriber</code>
     */
    GenericMqttSubscriber buildSubscriber(GenericMqttConfig config);

}
