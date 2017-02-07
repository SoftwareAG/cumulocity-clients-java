package com.cumulocity.sdk.services.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.sms.client.SmsMessagingApi;
import com.cumulocity.sms.client.SmsMessagingApiImpl;
import com.cumulocity.email.client.EmailApi;
import com.cumulocity.email.client.EmailApiImpl;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.services.client.rest.RestController;

public class ServicesPlatformImpl implements ServicesPlatform {

    private String host;

    private final RestTemplate authorizedTemplate;

    public ServicesPlatformImpl(String host, CumulocityCredentials credentials) {
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.host = host;
        authorizedTemplate = new RestController().authorizedTemplate(credentials);
    }

    @Override
    public SmsMessagingApi getSmsMessagingApi() {
        return new SmsMessagingApiImpl(host, authorizedTemplate);
    }

    @Override
    public EmailApi getEmailApi() {
        return new EmailApiImpl(host, authorizedTemplate);
    }
}
