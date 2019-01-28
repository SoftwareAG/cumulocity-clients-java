package com.cumulocity.sdk.paho;

import com.cumulocity.sdk.paho.exception.PahoDeviceSDKException;
import com.cumulocity.sdk.paho.connector.PahoMqttConnector;
import com.cumulocity.sdk.paho.model.ConnectionDetails;
import com.cumulocity.sdk.paho.operations.PahoMqttOperationsProvider;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

import static com.cumulocity.sdk.paho.util.PahoMqttTopicValidator.isTopicValidForPublish;
import static com.cumulocity.sdk.paho.util.PahoMqttTopicValidator.isTopicValidForSubscribe;

import static java.lang.String.format;

public class PahoMqttClientImpl implements PahoMqttClient {

    private String host;
    private String clientId;
    private String userName;
    private String password;
    private boolean cleanSession;

    private PahoMqttConnector connector;
    private PahoMqttOperationsProvider operationsProvider;

    public PahoMqttClientImpl() {}

    public PahoMqttClientImpl(String host, String clientId, String userName, String password) {
        this(host, clientId, userName, password, false);
    }

    /**
     *
     * @param host The url to connect to
     * @param clientId The unique clientId/deviceId to connect with
     * @param userName The username to connect with
     * @param password The password for the user
     * @param cleanSession Clear state at end of connection or not (durable or non-durable subscriptions),
     *                     by default set to false.
     */
    public PahoMqttClientImpl(String host, String clientId, String userName, String password, boolean cleanSession) {
        this.host = host;
        this.clientId = clientId;
        this.userName = userName;
        this.password = password;
        this.cleanSession = cleanSession;
    }

    @Override
    public void establishConnection() throws PahoDeviceSDKException {

        final ConnectionDetails connectionDetails = ConnectionDetails.builder().host(host)
                                                        .clientId(clientId)
                                                            .userName(userName)
                                                                .password(password)
                                                                    .cleanSession(cleanSession)
                                                                        .build();
        try {
            connector = new PahoMqttConnector(connectionDetails);
            operationsProvider = new PahoMqttOperationsProvider(connector);
        } catch (MqttException ex) {
            throw new PahoDeviceSDKException((format("Unable to construct client for clientId '%s' and connect to server" +
                    " '%s' : ", clientId, host)), ex);
        }
    }

    @Override
    public void publishToTopic(String topicName, int qos, String payload) throws PahoDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new PahoDeviceSDKException("Publish can happen only when client is initialized and connection to " +
                    "server established.");
        }

        if (! isTopicValidForPublish(topicName)) {
            throw new PahoDeviceSDKException("Invalid topic to publish.");
        }

        try {
            operationsProvider.publish(topicName, qos, payload);
        } catch (MqttException ex) {
            throw new PahoDeviceSDKException((format("Unable to publish message for clientId '%s' on topic '%s' : ",
                    clientId, topicName)), ex);
        }
    }

    @Override
    public void subscribeToTopic(String topicName, int qos, IMqttMessageListener messageListener) throws PahoDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new PahoDeviceSDKException("Subscribe can happen only when client is initialized and " +
                    "connection to server established.");
        }

        if (! isTopicValidForSubscribe(topicName)) {
            throw new PahoDeviceSDKException("Invalid topic to subscribe.");
        }

        try {
            operationsProvider.subscribe(topicName, qos, messageListener);
        } catch (MqttException ex) {
            throw new PahoDeviceSDKException((format("Unable to subscribe to topic '%s' for client '%s' : ",
                    topicName, clientId)), ex);
        }
    }

    @Override
    public void disconnect() throws PahoDeviceSDKException {

        try {
            operationsProvider.disconnect();
        } catch (MqttException ex) {
            throw new PahoDeviceSDKException((format("Unable to disconnect client : '%s'", clientId)), ex);
        }
    }
}
