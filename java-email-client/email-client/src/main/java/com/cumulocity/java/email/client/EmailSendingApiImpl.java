package com.cumulocity.java.email.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.email.client.properties.Properties;
import com.cumulocity.java.email.client.sending.EmailSendingClient;
import com.cumulocity.model.email.Email;

public class EmailSendingApiImpl implements EmailSendingApi {
    
    @Autowired
    private EmailSendingClient emailSendingClient;
    
    private Properties properties = Properties.getInstance();
    
    public EmailSendingApiImpl(String host, RestTemplate authorizedTemplate) {
        properties.setBaseUrl(host);
        properties.setAuthorizedTemplate(authorizedTemplate);
    }

    @Override
    public void sendEmail(Email email) {
        emailSendingClient.sendEmail(email);
    }

}
