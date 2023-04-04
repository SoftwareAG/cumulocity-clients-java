package com.cumulocity.generic.mqtt.client;

public interface GenericMqttPublisherBuilder {

    /**
     * Specify the topic to which instance of {@link GenericMqttPublisher} will connect to.
     */
    GenericMqttPublisherBuilder topic(final String topic);

    /**
     * Connection timeout in milliseconds.
     */
    GenericMqttPublisherBuilder connectionTimeout(long timeoutInMillis);

    GenericMqttPublisher create();

}
