package com.cumulocity.sdk.paho.connector;

import com.cumulocity.sdk.paho.model.ConnectionDetails;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PahoMqttConnector {

    private static final String TCP = "tcp://";
    private static final String TCP_MQTT_PORT = "1883";

    private MqttAsyncClient client;

    public PahoMqttConnector(final ConnectionDetails connectionDetails) throws MqttException {
        this.createConnection(connectionDetails);
    }

    private void createConnection(final ConnectionDetails connectionDetails) throws MqttException {

        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(connectionDetails.getUserName());
        options.setPassword(connectionDetails.getPassword().toCharArray());
        options.setAutomaticReconnect(true);
        options.setCleanSession(connectionDetails.isCleanSession());

        client = new MqttAsyncClient(getServerURI(connectionDetails),
                connectionDetails.getClientId(), persistence);
        IMqttToken conToken = client.connect(options);
        conToken.waitForCompletion();
    }

    public void disconnectClient() throws MqttException {
        IMqttToken discToken = client.disconnect();
        discToken.waitForCompletion();
    }

    public MqttAsyncClient getClient() {
        return client;
    }

    private String getServerURI(ConnectionDetails connectionDetails) {
        return TCP + connectionDetails.getHost() + ":" + TCP_MQTT_PORT;
    }
}
