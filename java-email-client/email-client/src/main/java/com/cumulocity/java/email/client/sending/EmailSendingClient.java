package com.cumulocity.java.email.client.sending;

import org.springframework.http.HttpStatus;

import com.cumulocity.java.email.client.rest.EmailRestRequest;
import com.cumulocity.model.email.Email;

public class EmailSendingClient {

    private final EmailRestRequest emailRestRequest;

    public EmailSendingClient() {
        emailRestRequest = new EmailRestRequest();
    }

    public HttpStatus sendEmail(Email email) {
        return emailRestRequest.doPost(email);
    }

}
