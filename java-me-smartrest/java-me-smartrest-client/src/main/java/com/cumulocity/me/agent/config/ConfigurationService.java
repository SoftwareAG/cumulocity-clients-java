package com.cumulocity.me.agent.config;

import java.io.IOException;

import com.cumulocity.me.agent.config.model.Configuration;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.config.model.DefaultConfiguration;

public class ConfigurationService {
    private static final Configuration DEFAULT_CONFIG = DefaultConfiguration.build();
    
    private final ConfigurationReader reader;
    private final ConfigurationWriter writer;
    private Configuration configuration;
    
    public ConfigurationService(ConfigurationReader reader, ConfigurationWriter writer) throws IOException{
        this.reader = reader;
        this.writer = writer;
        this.configuration = reader.read();
    }
    
    public String get(String key){
        return firstNonNull(key);
    }
    
    public String get(ConfigurationKey key){
        return firstNonNull(key.getKey());
    }
    
    public int getInt(String key){
        return Integer.parseInt(firstNonNull(key));
    }
    
    public int getInt(ConfigurationKey key){
        return Integer.parseInt(firstNonNull(key.getKey()));
    }
    
    public double getDouble(String key){
        return Double.parseDouble(firstNonNull(key));
    }
    
    public double getDouble(ConfigurationKey key){
        return Double.parseDouble(firstNonNull(key.getKey()));
    }
     
    public void set(String key, String value){
        configuration.set(key, value);
    }
    
    public void set(ConfigurationKey key, String value){
        configuration.set(key.getKey(), value);
    }
    
    public void set(String key, int value){
        configuration.set(key, Integer.toString(value));
    }
    
    public void set(ConfigurationKey key, int value){
        configuration.set(key.getKey(), Integer.toString(value));
    }
    
    public void set(String key, double value){
        configuration.set(key, Double.toString(value));
    }
    
    public void set(ConfigurationKey key, double value){
        configuration.set(key.getKey(), Double.toString(value));
    }
    
    public void remove(String key){
        configuration.remove(key);
    }
    
    public void remove(ConfigurationKey key){
        configuration.remove(key.getKey());
    }
    
    public void write() throws IOException{
        writer.write(configuration);
    }
    
    private String firstNonNull(String key){
        String value = configuration.get(key);
        if (value == null) {
            return DEFAULT_CONFIG.get(key);
        }
        return value;
    }
}
