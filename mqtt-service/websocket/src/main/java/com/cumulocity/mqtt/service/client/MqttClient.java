package com.cumulocity.mqtt.service.client;

import com.cumulocity.mqtt.service.client.websocket.MqttWebSocketClientBuilder;

import java.util.Optional;

/**
 * {@link MqttClient} is used to configure and create instances of {@link MqttPublisher} and {@link MqttSubscriber}.
 */
public interface MqttClient extends AutoCloseable {

    static MqttWebSocketClientBuilder webSocket() {
        return MqttWebSocketClientBuilder.builder();
    }

    /**
     * Creates an instance of {@link MqttPublisher} with the configured topic.
     *
     * @param config {@link PublisherConfig}
     * @return the instance of {@link MqttPublisher}
     */
    MqttPublisher buildPublisher(PublisherConfig config) throws MqttClientException;

    /**
     * Creates an instance of {@link MqttSubscriber} with the configured topic.
     *
     * @param config {@link SubscriberConfig}
     * @return the instance of {@link MqttSubscriber}
     */
    MqttSubscriber buildSubscriber(SubscriberConfig config) throws MqttClientException;

    /**
     * Returns the instance of {@link MqttPublisher} with the given id.
     *
     * @param id the publisher id
     * @return the instance of {@link MqttPublisher} or empty if not found
     */
    Optional<MqttPublisher> getPublisher(String id);

    /**
     * Returns the instance of {@link MqttSubscriber} with the given id.
     *
     * @param id the subscriber id
     * @return the instance of {@link MqttSubscriber} or empty if not found
     */
    Optional<MqttSubscriber> getSubscriber(String id);

    /**
     * Closes the instance of {@link MqttPublisher} with the given id and stop watching it by the client.
     *
     * @param id the publisher id
     */
    void closePublisher(String id);

    /**
     * Closes the instance of {@link MqttSubscriber} with the given id and stop watching it by the client.
     *
     * @param id the subscriber id
     */
    void closeSubscriber(String id);

    /**
     * Closes all instances of {@link MqttPublisher} and {@link MqttSubscriber} and the whole client.
     */
    @Override
    void close();

}
