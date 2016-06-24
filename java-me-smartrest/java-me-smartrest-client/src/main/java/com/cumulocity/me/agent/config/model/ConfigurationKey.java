package com.cumulocity.me.agent.config.model;
public class ConfigurationKey {
    public static final ConfigurationKey CONNECTION_HOST_URL = new ConfigurationKey("connection.host.url", "http://developer.cumulocity.com");
    public static final ConfigurationKey CONNECTION_SETUP_PARAMS_STANDARD = new ConfigurationKey("connection.setup.params", ";bearer_type=gprs;access_point=internet.m2mportal.de;username=m2m;password=sim");
    public static final ConfigurationKey CONNECTION_SETUP_PARAMS_REALTIME = new ConfigurationKey("connection.setup.params.realtime", ";bearer_type=gprs;access_point=internet.m2mportal.de;username=m2m;password=sim;timeout=7200");

    
    public static final ConfigurationKey AGENT_USER_CREDENTIALS = new ConfigurationKey("agent.user.credentials", null);
    public static final ConfigurationKey AGENT_BUFFER_SEND_INTERVAL = new ConfigurationKey("agent.buffer.send.interval", "1000");
    public static final ConfigurationKey AGENT_MONITORING_REQUIRED_INTERVAL = new ConfigurationKey("agent.monitoring.required.interval", "60");

    public static final ConfigurationKey AGENT_LOGGER_APPENDER =  new ConfigurationKey("agent.logger.appender", null);

    
    private final String key;
    private final String defaultValue;
    
    public static ConfigurationKey[] getAll() {
        return new ConfigurationKey[]{
            CONNECTION_HOST_URL,
            CONNECTION_SETUP_PARAMS_STANDARD,
            CONNECTION_SETUP_PARAMS_REALTIME,
            AGENT_USER_CREDENTIALS,
            AGENT_BUFFER_SEND_INTERVAL,
            AGENT_MONITORING_REQUIRED_INTERVAL,
            AGENT_LOGGER_APPENDER
        };
    }
    
    public ConfigurationKey(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getKey() {
        return key;
    }
}
