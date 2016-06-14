package com.cumulocity.me.agent.config.model;

import java.util.Enumeration;
import java.util.Hashtable;

import com.cumulocity.me.agent.util.StringStringMap;

public class Configuration {
    private final StringStringMap configMap;

    
    public Configuration() {
        this.configMap = new StringStringMap();
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
