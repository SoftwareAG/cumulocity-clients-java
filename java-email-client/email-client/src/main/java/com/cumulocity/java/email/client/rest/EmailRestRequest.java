package com.cumulocity.java.email.client.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cumulocity.java.email.client.properties.Properties;
import com.cumulocity.model.email.Email;

public class EmailRestRequest {
    private Properties properties = Properties.getInstance();

    public HttpStatus doPost(Email email) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        HttpEntity<Email> request = new HttpEntity<Email>(email, headers);
        ResponseEntity<Void> response = properties.getAuthorizedTemplate().postForEntity(emailApiEndpoint(), request, Void.class);
        return response.getStatusCode();
    }

    private String emailApiEndpoint() {
        return properties.getBaseUrl() + "email/emails/";
    }

}
