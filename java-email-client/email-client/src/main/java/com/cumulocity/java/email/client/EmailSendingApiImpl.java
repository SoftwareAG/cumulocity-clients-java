package com.cumulocity.java.email.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.java.email.client.properties.Properties;
import com.cumulocity.java.email.client.sending.EmailSendingClient;
import com.cumulocity.model.email.Email;

public class EmailSendingApiImpl implements EmailSendingApi {
    private final EmailSendingClient emailSendingClient;

    private Properties properties = Properties.getInstance();

    public EmailSendingApiImpl(String host, RestTemplate authorizedTemplate) {
        emailSendingClient = new EmailSendingClient();
        properties.setBaseUrl(host);
        properties.setAuthorizedTemplate(authorizedTemplate);
    }

    @Override
    public HttpStatus sendEmail(Email email) {
        return emailSendingClient.sendEmail(email);
    }

}
