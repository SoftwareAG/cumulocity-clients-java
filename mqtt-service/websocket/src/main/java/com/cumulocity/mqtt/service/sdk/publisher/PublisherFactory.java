package com.cumulocity.mqtt.service.sdk.publisher;

public interface PublisherFactory {

    Publisher build(PublisherConfig config);

}
