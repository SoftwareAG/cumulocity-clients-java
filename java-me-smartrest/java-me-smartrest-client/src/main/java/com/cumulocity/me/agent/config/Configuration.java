package com.cumulocity.me.agent.config;

import java.util.Enumeration;
import java.util.Hashtable;

public class Configuration {
    private final ConfigMap configMap;

    
    public Configuration() {
        this.configMap = new ConfigMap();
    }
    
    public void set(String key, String value){
        configMap.put(key, value);
    }
        
    public String get(String key){
        return configMap.get(key);
    }
    
    public void remove(String key){
        configMap.remove(key);
    }
    
    public Enumeration getKeys(){
        return configMap.keys();
    }
}
