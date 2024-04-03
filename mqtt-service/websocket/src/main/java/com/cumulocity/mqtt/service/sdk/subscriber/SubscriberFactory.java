package com.cumulocity.mqtt.service.sdk.subscriber;

public interface SubscriberFactory {

    Subscriber build(SubscriberConfig config);

}
