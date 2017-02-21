package com.cumulocity.email.client.sending;

import org.springframework.http.HttpStatus;

import com.cumulocity.email.client.properties.Properties;
import com.cumulocity.email.client.rest.EmailRestRequest;
import com.cumulocity.model.email.Email;

public class EmailSendingClient {

    private final EmailRestRequest emailRestRequest;

    public EmailSendingClient(Properties properties) {
        emailRestRequest = new EmailRestRequest(properties);
    }

    public HttpStatus sendEmail(Email email) {
        return emailRestRequest.doPost(email);
    }

}
