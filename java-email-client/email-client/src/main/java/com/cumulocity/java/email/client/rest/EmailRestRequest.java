package com.cumulocity.java.email.client.rest;

import com.cumulocity.java.email.client.properties.Properties;
import com.cumulocity.model.email.Email;

public class EmailRestRequest {
    private Properties properties = Properties.getInstance();

    public void doPost(Email email) {
        properties.getAuthorizedTemplate().postForEntity(emailApiEndpoint(), email, Email.class);
    }

    private String emailApiEndpoint() {
        return properties.getBaseUrl() + "/email/emails/";
    }

}
