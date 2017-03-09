package com.cumulocity.email.client;

import com.cumulocity.model.email.Email;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

import static org.apache.http.client.fluent.Request.Post;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.svenson.JSON.defaultJSON;

@RequiredArgsConstructor
public class EmailApiImpl implements EmailApi {
    private final String host;
    private final Executor authorizedTemplate;

    public int sendEmail(Email email) {
        try {
            final Request request = Post(emailApiEndpoint()).bodyString(defaultJSON().forValue(email), APPLICATION_JSON);
            final Response response = authorizedTemplate.execute(request);
            return response.returnResponse().getStatusLine().getStatusCode();
        } catch (IOException e) {
            throw new EmailClientException("Send email request failure", e);
        }
    }

    private String emailApiEndpoint() {
        return host + "email/emails/";
    }
}
