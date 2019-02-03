package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;
import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;
import com.cumulocity.sdk.mqtt.operations.MqttOperationsProvider;
import com.cumulocity.sdk.mqtt.operations.OperationsProvider;
import org.eclipse.paho.client.mqttv3.MqttException;

import static com.cumulocity.sdk.mqtt.util.MqttTopicValidator.isTopicValidForPublish;
import static com.cumulocity.sdk.mqtt.util.MqttTopicValidator.isTopicValidForSubscribe;

import static java.lang.String.format;

public class MqttClientImpl implements MqttClient {

    private final ConnectionDetails connectionDetails;

    private OperationsProvider operationsProvider;

    public MqttClientImpl(final ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    @Override
    public void establishConnection() throws MqttDeviceSDKException {

        try {
            operationsProvider = new MqttOperationsProvider();
            operationsProvider.createConnection(connectionDetails);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to construct client for clientId '%s' and connect to server" +
                    " '%s' : ", connectionDetails.getClientId(), connectionDetails.getHost())), ex);
        }
    }

    @Override
    public void publish(MqttMessageRequest message) throws MqttDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new MqttDeviceSDKException("Publish can happen only when client is initialized and connection to " +
                    "server established.");
        }

        if (! isTopicValidForPublish(message.getTopicName())) {
            throw new MqttDeviceSDKException("Invalid topic to publish.");
        }

        try {
            operationsProvider.publish(message);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to publish message for clientId '%s' on topic '%s' : ",
                    connectionDetails.getClientId(), message.getTopicName())), ex);
        }
    }

    @Override
    public void subscribe(MqttMessageRequest message, MqttMessageListener messageListener) throws MqttDeviceSDKException {

        if (! operationsProvider.isConnectionEstablished()) {
            throw new MqttDeviceSDKException("Subscribe can happen only when client is initialized and " +
                    "connection to server established.");
        }

        if (! isTopicValidForSubscribe(message.getTopicName())) {
            throw new MqttDeviceSDKException("Invalid topic to subscribe.");
        }

        try {
            operationsProvider.subscribe(message, messageListener);
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to subscribe to topic '%s' for client '%s' : ",
                    message.getTopicName(), connectionDetails.getClientId())), ex);
        }
    }

    @Override
    public void disconnect() throws MqttDeviceSDKException {

        try {
            operationsProvider.disconnect();
        } catch (MqttException ex) {
            throw new MqttDeviceSDKException((format("Unable to disconnect client : '%s'", connectionDetails.getClientId())), ex);
        }
    }
}
