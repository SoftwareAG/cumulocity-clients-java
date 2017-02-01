package com.cumulocity.sdk.services.client;

import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.sms.client.SmsMessagingApi;
import com.cumulocity.java.sms.client.SmsMessagingApiImpl;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityLogin;
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
}
