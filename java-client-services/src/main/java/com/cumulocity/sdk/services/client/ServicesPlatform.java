package com.cumulocity.sdk.services.client;

import com.cumulocity.email.client.EmailSendingApi;
import com.cumulocity.sms.client.SmsMessagingApi;

public interface ServicesPlatform {

    public SmsMessagingApi getSmsMessagingApi();

    public EmailSendingApi getEmailSendingApi();
}
