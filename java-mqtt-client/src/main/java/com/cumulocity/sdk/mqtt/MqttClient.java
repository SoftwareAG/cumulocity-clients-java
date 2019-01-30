package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;

public interface MqttClient {

    /**
     * Connects the client to the broker
     *
     * @throws MqttDeviceSDKException
     */
    void establishConnection() throws MqttDeviceSDKException;

    /**
     * Publishes a given message/payload with qos level 0 / 1 / 2
     * to a particular topic
     *
     * @param topicName
     * @param qos
     * @param payload
     *
     * @throws MqttDeviceSDKException
     */
    void publishToTopic(String topicName, int qos, String payload) throws MqttDeviceSDKException;

    /**
     * Subscribe to a particular topic
     *
     * @param topicName
     * @param qos
     * @param messageListener
     *
     * @throws MqttDeviceSDKException
     */
    void subscribeToTopic(String topicName, int qos, MqttMessageListener messageListener) throws MqttDeviceSDKException;

    /**
     * Disconnects the client from the broker
     *
     * @throws MqttDeviceSDKException
     */
    void disconnect() throws MqttDeviceSDKException;
}
