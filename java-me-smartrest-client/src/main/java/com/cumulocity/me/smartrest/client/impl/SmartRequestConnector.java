package com.cumulocity.me.smartrest.client.impl;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.SmartPlatform;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.util.IOUtils;

public class SmartRequestConnector {
    
    private static final String SMARTREST_API_PATH = "/s/";
    private final SmartPlatform smartPlatform;
    private HttpConnection connection;

    public SmartRequestConnector(SmartPlatform smartPlatform) {
        this.smartPlatform = smartPlatform;
    }
    
    public SmartResponse executeRequest() {
        return executeRequest(null);
    }
    
    public SmartResponse executeRequest(SmartRequest request) {
        try {
            return openConnection()
                .writeCommand()
                .writeHeaders()
                .writeBody(request)
                .buildResponse();
        } catch (IOException e) {
            throw new SDKException(e.getMessage(), e);
        }
    }

    private SmartRequestConnector openConnection() throws IOException {
        connection = (HttpConnection) Connector.open(smartPlatform.getHost() + SMARTREST_API_PATH);
        return this;
    }

    private SmartRequestConnector writeCommand() throws IOException {
        connection.setRequestMethod("POST");
        return this;
    }

    private SmartRequestConnector writeHeaders() throws IOException {
        connection.setRequestProperty("Authorization", smartPlatform.getAuthorization());
        connection.setRequestProperty("X-Id", smartPlatform.getXid());
        return this;
    }
    
    private SmartRequestConnector writeBody(SmartRequest request) throws IOException {
        if (request == null || request.getData() == null) {
            return this;
        }
        IOUtils.writeData(request.getData(), connection.openOutputStream());
        return this;
    }

    private SmartResponse buildResponse() throws IOException {
        SmartResponse response = new SmartResponseImpl(connection);
        return response;
    }

    public void close() {
        IOUtils.closeQuietly(connection);
    }

}
