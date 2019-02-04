package com.cumulocity.sdk.mqtt.operations;

import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface OperationsProvider {

    void createConnection(ConnectionDetails connectionDetails) throws MqttException;

    void publish(MqttMessageRequest message) throws MqttException;

    void subscribe(MqttMessageRequest message, MqttMessageListener messageListener) throws MqttException;

    void unsubscribe(String topic) throws MqttException;

    void disconnect() throws MqttException;

    boolean isConnectionEstablished();
}
