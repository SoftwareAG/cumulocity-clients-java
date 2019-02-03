package com.cumulocity.sdk.mqtt;

import com.cumulocity.sdk.mqtt.exception.MqttDeviceSDKException;
import com.cumulocity.sdk.mqtt.listener.MqttMessageListener;
import com.cumulocity.sdk.mqtt.model.MqttMessageRequest;

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
     * @param message
     *
     * @throws MqttDeviceSDKException
     */
    void publish(MqttMessageRequest message) throws MqttDeviceSDKException;

    /**
     * Subscribe to a particular topic
     *
     * @param message
     * @param messageListener
     *
     * @throws MqttDeviceSDKException
     */
    void subscribe(MqttMessageRequest message, MqttMessageListener messageListener) throws MqttDeviceSDKException;

    /**
     * Disconnects the client from the broker
     *
     * @throws MqttDeviceSDKException
     */
    void disconnect() throws MqttDeviceSDKException;
}
