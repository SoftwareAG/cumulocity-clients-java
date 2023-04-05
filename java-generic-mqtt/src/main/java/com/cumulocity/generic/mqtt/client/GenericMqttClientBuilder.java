package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

public interface GenericMqttClientBuilder {

    /**
     * Constructs a {@link GenericMqttClient} instance and sets it to connect to the specified URI. The
     * client does not attempt to connect automatically. The connection will only be established once you
     * obtain the instance of {@link GenericMqttPublisher} or {@link GenericMqttSubscriber} and invoke
     * {@link GenericMqttPublisher#publish(GenericMqttMessage)} or {@link GenericMqttSubscriber#subscribe(GenericMqttMessageListener)}
     */
    GenericMqttClient build();

}
