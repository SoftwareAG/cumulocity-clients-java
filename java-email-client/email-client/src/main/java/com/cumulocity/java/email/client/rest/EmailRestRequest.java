package com.cumulocity.java.email.client.rest;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cumulocity.java.email.client.properties.Properties;
import com.cumulocity.model.email.Email;

public class EmailRestRequest {
    private Properties properties = Properties.getInstance();

    public void doPost(Email email) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        HttpEntity<Email> request = new HttpEntity<Email>(email, headers);
        properties.getAuthorizedTemplate().postForEntity(emailApiEndpoint(), request, String.class);
    }

    private String emailApiEndpoint() {
        return properties.getBaseUrl() + "email/emails/";
    }

}
