package com.cumulocity.smartrest.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cumulocity.SDKException;
import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartExecutorService;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.smartrest.client.SmartRow;
import com.cumulocity.smartrest.util.Base64;
import com.cumulocity.smartrest.util.HttpConnection;
import com.cumulocity.smartrest.util.HttpConnectionFactory;
import com.cumulocity.smartrest.util.IOUtils;

public class SmartHttpConnection implements SmartConnection {
    
    private final String host;
    private String authorization; 
    private String params = null;
    private int mode = -1;
    private boolean timeout;
    private HttpConnectionFactory connector;
    private HttpConnection connection;
    private InputStream input;
    private OutputStream output;
    private SmartHeartBeatWatcher heartBeatWatcher;
    private final SmartExecutorService executorService;
    
    public SmartHttpConnection(String host, String authorization, SmartExecutorService executorService, HttpConnectionFactory connector) {
        this.host = host;
        this.authorization = authorization;
        this.executorService = executorService;
        this.connector = connector;
    }
    
    public SmartHttpConnection(String host, String authorization, HttpConnectionFactory connector) {
        this(host, authorization, new SmartExecutorServiceImpl(), connector);
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, HttpConnectionFactory connector) {
        this(host, "Basic " + Base64.encode(tenant + "/" + username + ":" + password), new SmartExecutorServiceImpl(), connector);
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, SmartExecutorService executorService, HttpConnectionFactory connector) {
        this(host, "Basic " + Base64.encode(tenant + "/" + username + ":" + password), executorService, connector);
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
            
            response = executeRequest(null, new SmartRequestImpl(SmartConnection.BOOTSTRAP_REQUEST_CODE, id));
            
            if (response != null) {
                responseRow = response.getRow(0);
            }
        } while(response == null || responseRow == null || responseRow.getMessageId() != SmartConnection.BOOTSTRAP_RESPONSE_CODE);
        String[] data = responseRow.getData();
        authorization = "Basic " + Base64.encode(data[1] + "/" + data[2] + ":" + data[3]);
        
        return authorization;
    }
    
    public String registerTemplate(String xid, String templates) throws SDKException {
        SmartResponse respCheckRegistration = executeRequest(xid, new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, ""));
        if (respCheckRegistration == null) {
            throw new SDKException("Could not connect to server");
        }
        SmartRow responseRow = respCheckRegistration.getRow(0);
        if (responseRow == null) {
            throw new SDKException("Template registration failed");
        }
        int codeCheck = responseRow.getMessageId();
        if (codeCheck != 20) {
            SmartRequest request = new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, templates);
            SmartResponse respRegister = executeRequest(xid, request);
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

    public SmartResponse executeRequest(String xid, SmartRequest request) {
        try {
            return openConnection(request.getPath())
                    .writeCommand()
                    .writeHeaders(xid)
                    .writeBody(request)
                    .buildResponse();
        } catch (IOException e) {
            throw new SDKException("I/O error!", e);
        } finally {
            closeConnection();
        }
    }
    
    public SmartResponse executeLongPollingRequest(String xid, SmartRequest request) {
        try {
            return openConnection(request.getPath())
                    .writeCommand()
                    .writeHeaders(xid)
                    .writeBody(request)
                    .startHeartBeatWatcher()
                    .buildResponse();
        } catch (IOException e) {
            throw new SDKException("I/O error!", e);
        } finally {
            if (heartBeatWatcher != null) {
                heartBeatWatcher.stop();
                heartBeatWatcher = null;
            }
            closeConnection();
        }
    }
    
    public void executeRequestAsync(String xid, SmartRequest request, SmartResponseEvaluator evaluator) {
        executorService.execute(new SmartRequestAsyncRunner(this, xid, request, evaluator));
    }
    
    public void closeConnection() {
        IOUtils.closeQuietly(output);
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(connection);
    }
    
    private SmartHttpConnection openConnection(String path) throws IOException {
        String url = host + path;
        if (params != null) {
            url = url + params;
        }
        if (mode != -1) {
            connection = (HttpConnection) connector.open(url, mode, timeout);
        } else {
            connection = (HttpConnection) connector.open(url);
        }
        return this;
    }

    private SmartHttpConnection writeCommand() throws IOException {
        connection.setRequestMethod("POST");
        return this;
    }

    private SmartHttpConnection writeHeaders(String xid) throws IOException {
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "text/plain");
        if (xid != null) {
            connection.setRequestProperty("X-Id", xid);
        }
        return this;
    }
    
    private SmartHttpConnection writeBody(SmartRequest request) throws IOException {
        if (request == null || request.getData() == null) {
            return this;
        }
        output = connection.openOutputStream();
        IOUtils.writeData(request.getData().getBytes(), output);
        return this;
    }

    private SmartResponse buildResponse() throws IOException {
        try {
            return interruptableReading();
        } catch (InterruptedException e) {
            return null;
        }
    }
    
    private SmartResponse interruptableReading() throws InterruptedException, IOException {
        input = connection.openInputStream();
        int responseCode = connection.getResponseCode();
        String responseMessage = connection.getResponseMessage();
        String body = readData();
        closeConnection();
        SmartResponse response = new SmartResponseImpl(responseCode, responseMessage, body);
        return response;
    }
    
    private synchronized String readData() {
        final int maxNumberOfAttempts = 3;
        final int nextAttemptTimeout = 250;
        int consecutiveFailedAttemptsCount = 0;
        boolean lookForHeartbeat = true;
        StringBuffer line = new StringBuffer();
        try {
            do {
                int c;
                while ((c = input.read()) != -1) {
                    if (lookForHeartbeat) {
                        lookForHeartbeat = isHeartbeat(c);
                    }
                    if (!lookForHeartbeat) {
                        consecutiveFailedAttemptsCount = 0;
                        line.append((char)c);
                    }
                }
                if (consecutiveFailedAttemptsCount < maxNumberOfAttempts) {
                    consecutiveFailedAttemptsCount++;
                    Thread.sleep(nextAttemptTimeout);
                } else {
                    break;
                }
            } while(true);
            return line.toString();
        } catch(IOException e) {
            throw new SDKException("I/O error!", e);
        } catch (InterruptedException e) {
            return null;
        }
    }
    
    private synchronized boolean isHeartbeat(int c) {
        if (c == 32) {
            if (heartBeatWatcher != null) {
                heartBeatWatcher.heartbeat();
            }
            return true;
        }
        return false;
    }
    
    private SmartHttpConnection startHeartBeatWatcher() {
        heartBeatWatcher = new SmartHeartBeatWatcher(this, Thread.currentThread());
        heartBeatWatcher.start();
        return this;
    }
}
