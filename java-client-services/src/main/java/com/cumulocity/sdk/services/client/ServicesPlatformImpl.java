package com.cumulocity.sdk.services.client;

import com.cumulocity.java.sms.client.SmsMessagingApi;
import com.cumulocity.java.sms.client.SmsMessagingApiImpl;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityLogin;

public class ServicesPlatformImpl implements ServicesPlatform {
    
    private String host;
    private String tenantId;
    private String user;
    private String password;
    private String applicationKey;
    private CumulocityLogin cumulocityLogin;
    private String requestOrigin;
    
    public ServicesPlatformImpl(String host, CumulocityCredentials credentials) {
        if (host.charAt(host.length() - 1) != '/') {
            host = host + "/";
        }
        this.host = host;
        this.tenantId = credentials.getTenantId();
        this.user = credentials.getUsername();
        this.password = credentials.getPassword();
        this.applicationKey = credentials.getApplicationKey();
        this.cumulocityLogin = credentials.getLogin();
        this.requestOrigin = credentials.getRequestOrigin();
    }

    @Override
    public SmsMessagingApi getSmsMessagingApi() {
        //restconnector?
        return new SmsMessagingApiImpl();
    }
}
