package com.cumulocity.me.agent.config.impl;

import com.cumulocity.me.agent.config.ConfigurationReader;
import com.cumulocity.me.agent.config.model.Configuration;
import com.cumulocity.me.util.StringUtils;

public class PropertiesStringConfigReader implements ConfigurationReader {

    private final String keyValueSeparator;
    private final String propertySeparator;

    private String config;

    public PropertiesStringConfigReader(String keyValueSeparator, String propertiesSeparator, String config) {
        this.keyValueSeparator = keyValueSeparator;
        this.propertySeparator = propertiesSeparator;
        this.config = config;
    }
    
    public PropertiesStringConfigReader(String keyValueSeparator, String propertiesSeparator) {
        this(keyValueSeparator, propertiesSeparator, null);
    }

    public Configuration read() {
        String[] properties = StringUtils.split(config, propertySeparator);
        Configuration config = new Configuration();
        for (int i = 0; i < properties.length; i++) {
            String currentProperty = properties[i];
            int splitIndex = currentProperty.indexOf(keyValueSeparator);
            try {
                String key = currentProperty.substring(0, splitIndex);
                String value = currentProperty.substring(splitIndex + 1);
                if (key != null && key.length() > 0) {
                    config.set(key, value);
                }
            } catch (IndexOutOfBoundsException e) {
                // ignore malformed property
            }
        }
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
}
