package com.cumulocity.generic.mqtt.client;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GenericMqttClientProperties {

    private final Map<String, Object> properties = new ConcurrentHashMap<>();

    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

}
