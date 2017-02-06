package com.cumulocity.java.email.client.properties;

import org.springframework.web.client.RestTemplate;

public class Properties {

    private String baseUrl;

    private RestTemplate authorizedTemplate;

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
