package com.cumulocity.generic.mqtt.client;

public interface GenericMqttSubscriberBuilder {

    /**
     * Specify the topic to which instance of {@link GenericMqttSubscriber} will connect to.
     */
    GenericMqttSubscriberBuilder topic(final String topic);

    /**
     * Connection timeout in milliseconds.
     */
    GenericMqttSubscriberBuilder connectionTimeout(long timeoutInMillis);

    GenericMqttSubscriber create();

}
