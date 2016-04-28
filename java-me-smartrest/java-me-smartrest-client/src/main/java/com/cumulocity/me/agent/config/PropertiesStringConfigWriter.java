package com.cumulocity.me.agent.config;

import java.io.IOException;
import java.util.Enumeration;

public class PropertiesStringConfigWriter implements ConfigurationWriter{

    private final String keyValueSeparator;
    private final String propertySeparator;
    
    private String output;

    public PropertiesStringConfigWriter(String keyValueSeparator, String propertySeparator) {
        this.keyValueSeparator = keyValueSeparator;
        this.propertySeparator = propertySeparator;
    }

    public void write(Configuration configuration) {
        StringBuffer buffer = new StringBuffer();
        Enumeration e = configuration.getKeys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = configuration.get(key);
            buffer.append(key).append(keyValueSeparator).append(value).append(propertySeparator);
        }
        output = buffer.toString();
    }

    public String getOutput() {
        return output;
    }
}
