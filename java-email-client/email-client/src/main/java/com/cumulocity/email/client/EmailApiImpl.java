package com.cumulocity.email.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import com.cumulocity.email.client.properties.Properties;
import com.cumulocity.email.client.sending.EmailSendingClient;
import com.cumulocity.model.email.Email;

public class EmailApiImpl implements EmailApi {
    private final EmailSendingClient emailSendingClient;

    public EmailApiImpl(String host, RestTemplate authorizedTemplate) {
        Properties properties = new Properties();
        emailSendingClient = new EmailSendingClient(properties);
        properties.setBaseUrl(host);
        properties.setAuthorizedTemplate(authorizedTemplate);
    }

    @Override
    public HttpStatus sendEmail(Email email) {
        return emailSendingClient.sendEmail(email);
    }

}
