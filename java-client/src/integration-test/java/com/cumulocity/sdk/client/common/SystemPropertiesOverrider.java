package com.cumulocity.sdk.client.common;

import java.util.Map;

public class SystemPropertiesOverrider {
    private final Map<Object, Object> properties;

    public SystemPropertiesOverrider(Map<Object, Object> properties) {
        this.properties = properties;
    }

    public String get(String key) {
        String system = System.getProperty(key);
        return system != null ? system : (String) properties.get(key);
    }
}
