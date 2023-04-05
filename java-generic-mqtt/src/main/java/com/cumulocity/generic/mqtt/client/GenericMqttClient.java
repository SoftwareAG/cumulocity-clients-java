package com.cumulocity.generic.mqtt.client;

/**
 * {@link GenericMqttClient} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttClient extends AutoCloseable {

    GenericMqttPublisher buildPublisher(String topic, GenericMqttClientProperties properties);

    GenericMqttSubscriber buildSubscriber(String topic, GenericMqttClientProperties properties);

}
