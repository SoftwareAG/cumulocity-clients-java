package com.cumulocity.mqtt.service.client;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public abstract class AbstractMqttClient implements MqttClient {

    private final Map<String, MqttPublisher> publishers = new ConcurrentHashMap<>();
    private final Map<String, MqttSubscriber> subscribers = new ConcurrentHashMap<>();

    protected abstract MqttPublisher createPublisherInstance(final PublisherConfig config);

    protected abstract MqttSubscriber createSubscriberInstance(final SubscriberConfig config);

    @Override
    public MqttPublisher buildPublisher(final PublisherConfig config) {
        final MqttPublisher publisher = createPublisherInstance(config);
        publishers.put(config.getId(), publisher);
        return publisher;
    }

    @Override
    public MqttSubscriber buildSubscriber(final SubscriberConfig config) {
        final MqttSubscriber subscriber = createSubscriberInstance(config);
        subscribers.put(config.getId(), subscriber);
        return subscriber;
    }

    @Override
    public Optional<MqttPublisher> getPublisher(final String id) {
        return ofNullable(publishers.get(id));
    }

    @Override
    public Optional<MqttSubscriber> getSubscriber(final String id) {
        return ofNullable(subscribers.get(id));
    }

    @Override
    public void closePublisher(final String id) {
        ofNullable(publishers.remove(id)).ifPresent(MqttPublisher::close);
    }

    @Override
    public void closeSubscriber(final String id) {
        ofNullable(subscribers.remove(id)).ifPresent(MqttSubscriber::close);
    }

    @Override
    public void close() {
        publishers.values().stream()
                .filter(MqttPublisher::isConnected)
                .forEach(MqttPublisher::close);
        publishers.clear();

        subscribers.values().stream()
                .filter(MqttSubscriber::isConnected)
                .forEach(MqttSubscriber::close);
        subscribers.clear();
    }
}
