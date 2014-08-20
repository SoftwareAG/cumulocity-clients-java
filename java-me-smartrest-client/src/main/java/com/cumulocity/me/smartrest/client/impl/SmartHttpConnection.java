package com.cumulocity.me.smartrest.client.impl;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.util.Base64;
import com.cumulocity.me.util.IOUtils;

public class SmartHttpConnection implements SmartConnection {
    
    private final String host;
    private String authorization; 
    private String xid;
    private String params;
    private int mode;
    private boolean timeout;
    private boolean useParams;
    private boolean useMode;
    private HttpConnection connection;
    
    public SmartHttpConnection(String host, String xid) {
        this.host = host;
        this.xid = xid;
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, String xid) {
        this.host = host;
        this.authorization = "Basic " + Base64.encode(tenant + "/" + username + ":" + password);
        this.xid = xid;
        this.useParams = false;
        this.useMode = false;
    }
    
    public void setupConnection(String params) {
        this.params = params;
        this.useParams = true;
    }
    
    public void setupConnection(int mode, boolean timeout) {
        this.mode = mode;
        this.timeout = timeout;
        this.useMode = true;
    }
    
    public String bootstrap() {
        // TODO: return auth header
        return null;
    }
    
    public String templateRegistration(String templates) {
        SmartResponse respCheckRegistration = executeRequest(new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, ""));
        int codeCheck = respCheckRegistration.getDataRows()[0].getMessageId();
        if (codeCheck != 20) {
            SmartRequest request = new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, templates);
            SmartResponse respRegister = executeRequest(request);
            int codeRegister = respRegister.getDataRows()[0].getMessageId();
            if (codeRegister != 20) {
                throw new SDKException(codeRegister, respRegister.getDataRows()[0].getData()[0]);
            }
            xid = String.valueOf(respRegister.getDataRows()[0].getData()[0]);
        } else {
            xid = String.valueOf(respCheckRegistration.getDataRows()[0].getData()[0]);
        } 
        closeConnection();
        return xid;
    }
    
    public SmartResponse executeRequest(SmartRequest request) {
        try {
            return openConnection(request.getPath())
                    .writeCommand()
                    .writeHeaders()
                    .writeBody(request)
                    .buildResponse();;
        } catch (IOException e) {
            throw new SDKException(e.getMessage(), e);
        }
    }
    
    private SmartHttpConnection openConnection(String path) throws IOException {
        String url = host + path;
        if (useParams) {
            url = url + params;    
        }
        if (useMode) {
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

    private SmartHttpConnection writeHeaders() throws IOException {
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("X-Id", xid);
        connection.setRequestProperty("Content-Type", "text/plain");
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
        SmartResponse response = new SmartResponseImpl(connection);
        return response;
    }

    public void closeConnection() {
        IOUtils.closeQuietly(connection);
    } 

}
