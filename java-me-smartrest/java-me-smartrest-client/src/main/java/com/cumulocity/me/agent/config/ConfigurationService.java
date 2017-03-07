package com.cumulocity.me.agent.config;

import java.io.IOException;
import java.util.Enumeration;

import com.cumulocity.me.agent.config.model.Configuration;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.config.model.DefaultConfiguration;
import com.cumulocity.me.agent.util.BooleanParser;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class ConfigurationService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);
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
        return firstNonNull(key);
    }
    
    public Integer getInt(String key){
        String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return new Integer(Integer.parseInt(string));
    }
    
    public Integer getInt(ConfigurationKey key){
    	String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return new Integer(Integer.parseInt(string));
    }
    
    public Double getDouble(String key){
    	String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return new Double(Double.parseDouble(string));
    }
    
    public Double getDouble(ConfigurationKey key){
    	String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return new Double(Double.parseDouble(string));
    }
    
    public Boolean getBoolean(String key) {
    	String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return BooleanParser.parse(string);
	}
    
    public Boolean getBoolean(ConfigurationKey key) {
    	String string = firstNonNull(key);
        if (string == null) {
			return null;
		}
    	return BooleanParser.parse(string);
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
    
    public void set(String key, boolean value){
        configuration.set(key, BooleanParser.parse(value));
    }
    
    public void set(ConfigurationKey key, boolean value){
        configuration.set(key.getKey(), BooleanParser.parse(value));
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

    public Enumeration getKeys() {
        return configuration.getKeys();
    }
    
    private String firstNonNull(String key){
        String value = configuration.get(key);
        if (value == null) {
            return DEFAULT_CONFIG.get(key);
        }
        return value;
    }

    private String firstNonNull(ConfigurationKey key){
        String value = firstNonNull(key.getKey());
        if (value == null) {
            return key.getDefaultValue();
        }
        LOG.debug("Read config parameter '" + key.getKey() + "', value is '" + value + "'");
        return value;
    }
}
