package com.cumulocity.java.email.client;

import com.cumulocity.model.email.Email;

public interface EmailSendingApi {
    public void sendEmail(Email email);
}
