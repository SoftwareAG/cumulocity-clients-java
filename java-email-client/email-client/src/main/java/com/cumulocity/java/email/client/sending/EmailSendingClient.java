package com.cumulocity.java.email.client.sending;

import com.cumulocity.java.email.client.rest.EmailRestRequest;
import com.cumulocity.model.email.Email;

public class EmailSendingClient {

    private final EmailRestRequest emailRestRequest;

    public EmailSendingClient() {
        emailRestRequest = new EmailRestRequest();
    }

    public void sendEmail(Email email) {
        emailRestRequest.doPost(email);
    }

}
