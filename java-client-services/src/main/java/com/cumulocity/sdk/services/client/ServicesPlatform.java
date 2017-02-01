package com.cumulocity.sdk.services.client;

import com.cumulocity.java.email.client.EmailSendingApi;
import com.cumulocity.java.sms.client.SmsMessagingApi;

public interface ServicesPlatform {

    public SmsMessagingApi getSmsMessagingApi();

    public EmailSendingApi getEmailSendingApi();
}
