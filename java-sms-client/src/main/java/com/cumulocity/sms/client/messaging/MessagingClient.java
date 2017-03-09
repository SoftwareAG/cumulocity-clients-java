package com.cumulocity.sms.client.messaging;

import com.cumulocity.sms.client.model.Address;
import com.cumulocity.sms.client.model.IncomingMessage;
import com.cumulocity.sms.client.model.IncomingMessages;
import com.cumulocity.sms.client.model.OutgoingMessageRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.apache.http.client.fluent.Request.Get;
import static org.apache.http.client.fluent.Request.Post;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.svenson.JSON.defaultJSON;
import static org.svenson.JSONParser.defaultJSONParser;

@RequiredArgsConstructor
public class MessagingClient {
    
    protected final String host;
    protected final Executor authorizedTemplate;

    public String sendMessage(@NonNull Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        try {
            final String body = defaultJSON().forValue(outgoingMessageRequest);
            final Request request = Post(sendEndpoint() + senderAddress.asUrlParameter() + "/requests").bodyString(body, APPLICATION_JSON);
            final Response response = authorizedTemplate.execute(request);
            return parseResponseContent(response);

        } catch (IOException e) {
            throw new SmsClientException("Send sms request failure", e);
        }
    }

    public IncomingMessages getAllMessages(Address receiveAddress) {
        try {
            final Request request = Get(receiveEndpoint() + receiveAddress + "/messages");
            final Response response = authorizedTemplate.execute(request);
            return defaultJSONParser().parse(IncomingMessages.class, parseResponseContent(response));
        } catch(IOException e) {
            throw new SmsClientException("Get sms messages failure", e);
        }
    }

    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        try {
            final Request request = Get(receiveEndpoint() + receiveAddress + "/messages/" + messageId);
            final Response response = authorizedTemplate.execute(request);
            return defaultJSONParser().parse(IncomingMessage.class, parseResponseContent(response));
        } catch(IOException e) {
            throw new SmsClientException("Get sms message failure", e);
        }
    }

    private String sendEndpoint() {
        return microserviceApiEndpoint() + "outbound/";
    }

    private String receiveEndpoint() {
        return microserviceApiEndpoint() + "inbound/registrations/";
    }

    private String microserviceApiEndpoint() {
        return host + "service/sms/smsmessaging/";
    }

    @SneakyThrows
    private String parseResponseContent(Response response) {
        if  (response == null) {
            return null;
        }
        final HttpResponse httpResponse = response.returnResponse();
        if (httpResponse.getEntity() != null) {
            return EntityUtils.toString(httpResponse.getEntity());
        }
        return null;
    }
}
