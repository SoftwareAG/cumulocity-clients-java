package com.cumulocity.generic.mqtt.client;

public interface GenericMqttFactory {
    GenericMqttPublisher buildPublisher(GenericMqttConnectionConfig config);

    GenericMqttSubscriber buildSubscriber(GenericMqttConnectionConfig config);
}
