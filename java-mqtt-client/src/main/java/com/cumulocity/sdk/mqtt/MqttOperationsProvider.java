package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

@NoArgsConstructor
class MqttOperationsProvider implements OperationsProvider {

    private static final String TCP = "tcp://";
    private static final String TCP_MQTT_PORT = "1883";

    private MqttAsyncClient client;

    @Override
    public void createConnection(final ConnectionDetails connectionDetails) throws MqttException {

        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(connectionDetails.getUserName());
        options.setPassword(connectionDetails.getPassword().toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(connectionDetails.isCleanSession());

        client = new MqttAsyncClient(getServerURI(connectionDetails),
                connectionDetails.getClientId(), persistence);
        final IMqttToken conToken = client.connect(options);
        conToken.waitForCompletion();
    }

    @Override
    public void publish(MqttMessageRequest message) throws MqttException {

        // Connect to the MQTT server
        if (! client.isConnected()) {
            IMqttToken conToken = client.connect();
            conToken.waitForCompletion();
        }

        // Construct the message to send
        MqttMessage pubMessage = new MqttMessage(message.getMessageContent().getBytes());
        pubMessage.setQos(message.getQoS().getValue());

        // Send the message to the server, control is returned as soon
        // as the MQTT client has accepted to deliver the message.
        final IMqttDeliveryToken pubToken = client.publish(message.getTopicName(), pubMessage);
        pubToken.waitForCompletion();
    }

    @Override
    public void subscribe(MqttMessageRequest message, MqttMessageListener messageListener) throws MqttException {

        // Connect to the MQTT server
        if (! client.isConnected()) {
            IMqttToken conToken = client.connect();
            conToken.waitForCompletion();
        }

        // Subscribe to the requested topic.
        final MqttMessageAdapter messageAdapter = new MqttMessageAdapter(messageListener);
        final IMqttToken subToken = client.subscribe(message.getTopicName(), message.getQoS().getValue(), null,
                null, messageAdapter);
        subToken.waitForCompletion();
    }

    @Override
    public void unsubscribe(String topic) throws MqttException {

        // Connect to the MQTT server
        if (! client.isConnected()) {
            IMqttToken conToken = client.connect();
            conToken.waitForCompletion();
        }

        // Unsubscribe from the requested topic
        final IMqttToken unSubToken = client.unsubscribe(topic);
        unSubToken.waitForCompletion();
    }

    @Override
    public void disconnect() throws MqttException {

        if (client.isConnected()) {
             final IMqttToken discToken = client.disconnect();
             discToken.waitForCompletion();
        }
    }

    @Override
    public boolean isConnectionEstablished() {
        return client.isConnected();
    }

    private String getServerURI(ConnectionDetails connectionDetails) {
        return TCP + connectionDetails.getHost() + ":" + TCP_MQTT_PORT;
    }
}
