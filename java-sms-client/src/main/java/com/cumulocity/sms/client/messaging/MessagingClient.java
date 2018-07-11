package com.cumulocity.sms.client.messaging;

import com.cumulocity.model.sms.Address;
import com.cumulocity.model.sms.IncomingMessage;
import com.cumulocity.model.sms.IncomingMessages;
import com.cumulocity.model.sms.OutgoingMessageRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Getter
@RequiredArgsConstructor
public class MessagingClient {

    public static String addLeadingSlash(String host) {
        if (host.charAt(host.length() - 1) != '/') {
            return host + "/";
        }
        return host;
    }

    private final String rootEndpoint;
    private final Executor authorizedTemplate;

    public String sendMessage(@NonNull Address senderAddress, OutgoingMessageRequest outgoingMessageRequest) {
        try {
            final String body = defaultJSON().forValue(outgoingMessageRequest);
            final String s = senderAddressAsUrlParameter(senderAddress);
            final String url = sendEndpoint() + s + "/requests";
            final Request request = Post(url).bodyString(body, APPLICATION_JSON);

            final Response response = authorizedTemplate.execute(request);
            return parseResponse(response);
        } catch (IOException e) {
            throw new SmsClientException("Send sms request failure", e);
        }
    }

    public IncomingMessages getAllMessages(Address receiveAddress) {
        try {
            final Request request = Get(receiveEndpoint() + receiveAddress + "/messages");
            final Response response = authorizedTemplate.execute(request);
            return defaultJSONParser().parse(IncomingMessages.class, parseResponse(response));
        } catch(IOException e) {
            throw new SmsClientException("Get sms messages failure", e);
        }
    }

    public IncomingMessage getMessage(Address receiveAddress, String messageId) {
        try {
            final Request request = Get(receiveEndpoint() + receiveAddress + "/messages/" + messageId);
            final Response response = authorizedTemplate.execute(request);
            return defaultJSONParser().parse(IncomingMessage.class, parseResponse(response));
        } catch(IOException e) {
            throw new SmsClientException("Get sms message failure", e);
        }
    }

    private String sendEndpoint() {
        return addLeadingSlash(rootEndpoint) + "outbound/";
    }

    private String receiveEndpoint() {
        return addLeadingSlash(rootEndpoint) + "inbound/registrations/";
    }

    @SneakyThrows
    private String parseResponse(Response response) {
        if  (response == null) {
            return null;
        }
        final HttpResponse httpResponse = response.returnResponse();
        final int statusCode = httpResponse.getStatusLine().getStatusCode();
        final String body = parseResponseBody(httpResponse);
        if (statusCode != 200) {
            throw new SmsClientException("Incorrect response: " + statusCode);
        }
        return body;
    }

    private String parseResponseBody(HttpResponse httpResponse) throws IOException {
        if (httpResponse.getEntity() != null) {
            return EntityUtils.toString(httpResponse.getEntity());
        }
        return null;
    }

    private String senderAddressAsUrlParameter(@NonNull Address senderAddress) {
        if (senderAddress.getNumber() == null) {
            return null;
        }
        return senderAddress.asUrlParameter();
    }
}
