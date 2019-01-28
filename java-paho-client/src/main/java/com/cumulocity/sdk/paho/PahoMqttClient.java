package com.cumulocity.sdk.paho;

import com.cumulocity.sdk.paho.exception.PahoDeviceSDKException;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

public interface PahoMqttClient {

    /**
     * Connects the client to the broker
     *
     * @throws PahoDeviceSDKException
     */
    void establishConnection() throws PahoDeviceSDKException;

    /**
     * Publishes a given message/payload with qos level 0 / 1 / 2
     * to a particular topic
     *
     * @param topicName
     * @param qos
     * @param payload
     *
     * @throws PahoDeviceSDKException
     */
    void publishToTopic(String topicName, int qos, String payload) throws PahoDeviceSDKException;

    /**
     * Subscribe to a particular topic
     *
     * @param topicName
     * @param qos
     * @param messageListener
     *
     * @throws PahoDeviceSDKException
     */
    void subscribeToTopic(String topicName, int qos, IMqttMessageListener messageListener) throws PahoDeviceSDKException;

    /**
     * Disconnects the client from the broker
     *
     * @throws PahoDeviceSDKException
     */
    void disconnect() throws PahoDeviceSDKException;
}
