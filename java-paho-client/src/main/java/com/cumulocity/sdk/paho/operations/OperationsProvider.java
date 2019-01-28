package com.cumulocity.sdk.paho.operations;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface OperationsProvider {

    void publish(String topicName, int qos, String payload) throws MqttException;

    void subscribe(String topicName, int qos, IMqttMessageListener messageListener) throws MqttException;

    void disconnect() throws MqttException;

    boolean isConnectionEstablished();
}
