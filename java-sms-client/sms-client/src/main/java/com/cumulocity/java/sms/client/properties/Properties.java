package com.cumulocity.java.sms.client.properties;

public class Properties {
    
    private static Properties properties = new Properties();
    private String baseUrl;
    
    public static Properties getInstance() {
        return properties;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
}
