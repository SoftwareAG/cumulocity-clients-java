package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.operations.MqttOperationsProvider;
import com.cumulocity.sdk.mqtt.operations.OperationsProvider;
import org.eclipse.paho.client.mqttv3.MqttException;

import static com.cumulocity.sdk.mqtt.util.MqttTopicValidator.isTopicValidForPublish;
import static com.cumulocity.sdk.mqtt.util.MqttTopicValidator.isTopicValidForSubscribe;

import static java.lang.String.format;

public class MqttClientImpl implements MqttClient {

    private String host;
    private String clientId;
    private String userName;
    private String password;
    private boolean cleanSession;

    private OperationsProvider operationsProvider;

    public MqttClientImpl() {}

    public MqttClientImpl(String host, String clientId, String userName, String password) {
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
    public MqttClientImpl(String host, String clientId, String userName, String password, boolean cleanSession) {
        this.host = host;
        this.clientId = clientId;
        this.userName = userName;
        this.password = password;
        this.cleanSession = cleanSession;
    }

    @Override
    public void establishConnection() throws MqttDeviceSDKException {

        final ConnectionDetails connectionDetails = ConnectionDetails.builder().host(host)
                                                        .clientId(clientId)
                                                            .userName(userName)
                                                                .password(password)
                                                                    .cleanSession(cleanSession)
                                                                        .build();
        try {
            operationsProvider = new MqttOperationsProvider();
            operationsProvider.createConnection(connectionDetails);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to construct client for clientId '%s' and connect to server" +
                    " '%s' : ", clientId, host)), ex);
        }
    }

    @Override
    public void publishToTopic(String topicName, int qos, String payload) throws MqttDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new MqttDeviceSDKException("Publish can happen only when client is initialized and connection to " +
                    "server established.");
        }

        if (! isTopicValidForPublish(topicName)) {
            throw new MqttDeviceSDKException("Invalid topic to publish.");
        }

        try {
            operationsProvider.publish(topicName, qos, payload);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to publish message for clientId '%s' on topic '%s' : ",
                    clientId, topicName)), ex);
        }
    }

    @Override
    public void subscribeToTopic(String topicName, int qos, MqttMessageListener messageListener) throws MqttDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new MqttDeviceSDKException("Subscribe can happen only when client is initialized and " +
                    "connection to server established.");
        }

        if (! isTopicValidForSubscribe(topicName)) {
            throw new MqttDeviceSDKException("Invalid topic to subscribe.");
        }

        try {
            operationsProvider.subscribe(topicName, qos, messageListener);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to subscribe to topic '%s' for client '%s' : ",
                    topicName, clientId)), ex);
        }
    }

    @Override
    public void disconnect() throws MqttDeviceSDKException {

        try {
            operationsProvider.disconnect();
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to disconnect client : '%s'", clientId)), ex);
        }
    }
}
