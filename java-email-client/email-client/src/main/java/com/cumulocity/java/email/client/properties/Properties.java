package com.cumulocity.java.email.client.properties;

import org.springframework.web.client.RestTemplate;

public class Properties {

    private static Properties properties = new Properties();

    private String baseUrl;

    private RestTemplate authorizedTemplate;

    public static Properties getInstance() {
        return properties;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RestTemplate getAuthorizedTemplate() {
        return authorizedTemplate;
    }

    public void setAuthorizedTemplate(RestTemplate authorizedTemplate) {
        this.authorizedTemplate = authorizedTemplate;
    }
}
