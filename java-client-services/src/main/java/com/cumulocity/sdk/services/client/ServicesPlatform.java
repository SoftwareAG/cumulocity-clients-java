package com.cumulocity.sdk.services.client;

import com.cumulocity.email.client.EmailApi;
import com.cumulocity.sms.client.SmsMessagingApi;

public interface ServicesPlatform {

    public SmsMessagingApi getSmsMessagingApi();

    public EmailApi getEmailApi();
}
