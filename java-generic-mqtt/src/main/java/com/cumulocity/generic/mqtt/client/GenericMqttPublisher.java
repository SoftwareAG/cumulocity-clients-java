package com.cumulocity.generic.mqtt.client;

import com.cumulocity.generic.mqtt.client.model.GenericMqttMessage;

public interface GenericMqttPublisher extends AutoCloseable {
    void publish(GenericMqttMessage genericMqttMessage);
}
