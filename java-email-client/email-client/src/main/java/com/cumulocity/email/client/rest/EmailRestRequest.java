package com.cumulocity.email.client.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.cumulocity.email.client.exception.EmailClientException;
import com.cumulocity.email.client.properties.Properties;
import com.cumulocity.model.email.Email;

public class EmailRestRequest {
    private final Properties properties;

    public EmailRestRequest(Properties properties) {
        this.properties = properties;
    }

    public HttpStatus doPost(Email email) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Content-Type", "application/json");
        HttpEntity<Email> request = new HttpEntity<Email>(email, headers);

        try {
            ResponseEntity<Void> response = properties.getAuthorizedTemplate().postForEntity(emailApiEndpoint(), request, Void.class);
            return response.getStatusCode();
        } catch (RestClientException e) {
            throw new EmailClientException("Send email request failure", e);
        }

    }

    private String emailApiEndpoint() {
        return properties.getBaseUrl() + "email/emails/";
    }

}
