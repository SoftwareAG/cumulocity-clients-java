package com.cumulocity.me.agent.config.model;

public class DefaultConfiguration {
    public static Configuration build() {
        Configuration configuration = new Configuration();
        ConfigurationKey[] parameters = ConfigurationKey.getAll();
        for (int i = 0; i < parameters.length; i++) {
            ConfigurationKey parameter = parameters[i];
            if (parameter.getDefaultValue() != null) {
                configuration.set(parameter.getKey(), parameter.getDefaultValue());
            }
        }
        return configuration;
    }
}
