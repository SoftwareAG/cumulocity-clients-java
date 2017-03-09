package com.cumulocity.sms.client.model;

public class ResourceReference {
    private String resourceURL;

    public ResourceReference() {
    }

    public ResourceReference(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }
}
