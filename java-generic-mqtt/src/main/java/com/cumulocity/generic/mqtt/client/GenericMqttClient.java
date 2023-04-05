package com.cumulocity.generic.mqtt.client;

import java.util.Properties;

/**
 * {@link GenericMqttClient} is used to configure and create instances of {@link GenericMqttPublisher} and {@link GenericMqttSubscriber}.
 */
public interface GenericMqttClient extends AutoCloseable {

    GenericMqttPublisher buildPublisher(String topic, Properties properties);

    GenericMqttSubscriber buildSubscriber(String topic, Properties properties);

}
