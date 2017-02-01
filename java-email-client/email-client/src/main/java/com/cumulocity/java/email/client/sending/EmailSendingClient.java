package com.cumulocity.java.email.client.sending;

import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.java.email.client.rest.EmailRestRequest;
import com.cumulocity.model.email.Email;

public class EmailSendingClient {
    
    @Autowired
    private EmailRestRequest emailRestRequest;

    public void sendEmail(Email email) {
        emailRestRequest.doPost(email);
    }

}
