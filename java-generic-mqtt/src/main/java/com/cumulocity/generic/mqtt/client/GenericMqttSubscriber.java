package com.cumulocity.generic.mqtt.client;

public interface GenericMqttSubscriber extends AutoCloseable {
    void subscribe(GenericMqttMessageListener messageListener);
}
