package com.cumulocity.me.smartrest.client.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.util.Base64;
import com.cumulocity.me.util.IOUtils;

public class SmartHttpConnection implements SmartConnection {
    
    private final String host;
    private String authorization; 
    private String xid;
    private String params = null;
    private int mode = -1;
    private boolean timeout;
    private boolean isRegistered;
    private HttpConnection connection;
    private InputStream input;
    
    public SmartHttpConnection(String host, String xid) {
        this.host = host;
        this.xid = xid;
        this.authorization = SmartConnection.DEVICEBOOTSTRAP_AUTHENTICATION;
        this.isRegistered = false;
    }
    
    public SmartHttpConnection(String host, String xid, String authorization) {
        this.host = host;
        this.xid = xid;
        this.authorization = authorization;
        this.isRegistered = true;
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, String xid) {
        this.host = host;
        this.xid = xid;
        this.authorization = "Basic " + Base64.encode(tenant + "/" + username + ":" + password);
        this.isRegistered = true;

    }
    
    public void setupConnection(String params) {
        this.params = params;
    }
    
    public void setupConnection(int mode, boolean timeout) {
        this.mode = mode;
        this.timeout = timeout;
    }
    
    public String bootstrap(String id) {
        SmartResponse response = null;
        SmartRow responseRow = null;
        do {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                continue;
            }
            response = executeRequest(new SmartRequestImpl(SmartConnection.BOOTSTRAP_REQUEST_CODE, id));
            if (response != null) {
                responseRow = response.getRow(0);
            }
        } while(response == null || responseRow == null || responseRow.getMessageId() != SmartConnection.BOOTSTRAP_RESPONSE_CODE);
        String[] data = responseRow.getData();
        authorization = "Basic " + Base64.encode(data[1] + "/" + data[2] + ":" + data[3]);
        isRegistered = true;
        return authorization;
    }
    
    public String templateRegistration(String templates) throws SDKException {
        if (!isRegistered) {
            throw new SDKException("No authorization is set. Use bootstrap first");
        }
        SmartResponse respCheckRegistration = executeRequest(new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, ""));
        if (respCheckRegistration == null) {
            throw new SDKException("Could not connect to server");
        }
        int codeCheck = respCheckRegistration.getRow(0).getMessageId();
        if (codeCheck != 20) {
            SmartRequest request = new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, templates);
            SmartResponse respRegister = executeRequest(request);
            if (respRegister == null) {
                throw new SDKException("Could not connect to server");
            }
            if (respRegister.isSuccessful()) {
                int codeRegister = respRegister.getRow(0).getMessageId();
                if (codeRegister != 20) {
                    throw new SDKException(codeRegister, respRegister.getRow(0).getData(0));
                }
                xid = String.valueOf(respRegister.getRow(0).getData(0));
            }
        } else {
            xid = String.valueOf(respCheckRegistration.getRow(0).getData(0));
        } 
        return xid;
    }

    public SmartResponse executeRequest(SmartRequest request) {
        try {
            return openConnection(request.getPath())
                    .writeCommand()
                    .writeHeaders(request)
                    .writeBody(request)
                    .buildResponse();
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(connection);
        }
    }
    
    public void executeRequestAsync(SmartRequest request, SmartResponseEvaluator evaluator) {
        SmartRequestAsyncRunner runner = new SmartRequestAsyncRunner(this, request, evaluator);
        runner.start();
    }
    
    public void closeConnection() {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(connection);
    }
    
    private SmartHttpConnection openConnection(String path) throws IOException {
        String url = host + path;
        if (params != null) {
            url = url + params;    
        }
        if (mode != -1) {
            connection = (HttpConnection) Connector.open(url, mode, timeout);
        } else {
            connection = (HttpConnection) Connector.open(url);
        }
        return this;
    }

    private SmartHttpConnection writeCommand() throws IOException {
        connection.setRequestMethod("POST");
        return this;
    }

    private SmartHttpConnection writeHeaders(SmartRequest request) throws IOException {
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "text/plain");
        if (isRegistered) {
            connection.setRequestProperty("X-Id", xid);
        }
        return this;
    }
    
    private SmartHttpConnection writeBody(SmartRequest request) throws IOException {
        if (request == null || request.getData() == null) {
            return this;
        }
        IOUtils.writeData(request.getData().getBytes(), connection.openOutputStream());
        return this;
    }

    private SmartResponse buildResponse() throws IOException {
        input = connection.openInputStream();
        SmartResponse response = new SmartResponseImpl(connection.getResponseCode(), connection.getResponseMessage(), IOUtils.readData(input));
        return response;
    }
    
    
}
