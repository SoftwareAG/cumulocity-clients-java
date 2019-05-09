package com.cumulocity.sdk.mqtt.model;

public class MqttTopics {

    public static final String TOPIC_SEPARATOR = "/";

    // smartrest exceptions
    public static final String BASE_SMARTREST_EXCEPTION_SUBSCRIBE = "s/e";

    // smartrest downlink static
    public static final String BASE_SMARTREST_STATIC_SUBSCRIBE = "s/ds";

    // persistent smartrest uplink static
    public static final String BASE_SMARTREST_STATIC_PUBLISH_PERSISTENT = "s/us";

    // persistent smartrest uplink legacy
    public static final String BASE_SMARTREST_LEGACY_PUBLISH_PERSISTENT = "s/ul";

    // smartrest downlink legacy
    public static final String BASE_SMARTREST_LEGACY_SUBSCRIBE = "s/dl";

    // persistent smartrest operations legacy
    public static final String BASE_SMARTREST_LEGACY_OPERATIONS_PERSISTENT = "s/ol";

    // persistent smartrest uplink custom template
    public static final String BASE_SMARTREST_CUSTOM_TEMPLATE_PUBLISH_PERSISTENT = "s/uc";

    // smartrest downlink custom template
    public static final String BASE_SMARTREST_CUSTOM_TEMPLATE_SUBSCRIBE = "s/dc";

    // persistent smartrest uplink template creation
    public static final String BASE_SMARTREST_TEMPLATE_CREATION_PUBLISH_PERSISTENT = "s/ut";

    // smartrest downlink template creation
    public static final String BASE_SMARTREST_TEMPLATE_CREATION_SUBSCRIBE = "s/dt";

    // uplink device credentials
    public static final String BASE_DEVICE_CREDENTIALS_PUBLISH = "s/ucr";

    // downlink device credentials
    public static final String BASE_DEVICE_CREDENTIALS_SUBSCRIBE = "s/dcr";
}
