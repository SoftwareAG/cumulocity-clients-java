package com.cumulocity.sdk.paho.operations;

import com.cumulocity.sdk.paho.connector.PahoMqttConnector;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

@AllArgsConstructor
public class PahoMqttOperationsProvider implements OperationsProvider {

    private PahoMqttConnector connector;

    @Override
    public void publish(String topicName, int qos, String payload) throws MqttException {

        final MqttAsyncClient client = connector.getClient();

        // Connect to the MQTT server
        if (! client.isConnected()) {
            IMqttToken conToken = client.connect();
            conToken.waitForCompletion();
        }

        // Construct the message to send
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);

        // Send the message to the server, control is returned as soon
        // as the MQTT client has accepted to deliver the message.
        final IMqttDeliveryToken pubToken = client.publish(topicName, message);
        pubToken.waitForCompletion();
    }

    @Override
    public void subscribe(String topicName, int qos, IMqttMessageListener messageListener) throws MqttException {

        final MqttAsyncClient client = connector.getClient();

        // Connect to the MQTT server
        if (! client.isConnected()) {
            IMqttToken conToken = client.connect();
            conToken.waitForCompletion();
        }

        // Subscribe to the requested topic.
        final IMqttToken subToken = client.subscribe(topicName, qos, null,
                null, messageListener);
        subToken.waitForCompletion();
    }

    @Override
    public void disconnect() throws MqttException {

        if (connector.getClient().isConnected()) {
            connector.disconnectClient();
        }
    }

    @Override
    public boolean isConnectionEstablished() {
        return connector.getClient().isConnected();
    }
}
