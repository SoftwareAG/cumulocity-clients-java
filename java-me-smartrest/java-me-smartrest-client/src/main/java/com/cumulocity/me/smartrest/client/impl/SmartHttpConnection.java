package com.cumulocity.me.smartrest.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.*;
import com.cumulocity.me.util.Base64;
import com.cumulocity.me.util.ConnectorWrapper;
import com.cumulocity.me.util.IOUtils;

public class SmartHttpConnection implements SmartConnection {
    
    private final String host;
    private String authorization; 
    private String xid;
    private String params = null;
    private int mode = -1;
    private boolean timeout;
    private HttpConnection connection;
    private InputStream input;
    private OutputStream output;
    private SmartHeartBeatWatcher heartBeatWatcher;
    private final SmartExecutorService executorService;
    private boolean isBootstrapping = false;
    private ConnectorWrapper connectorWrapper = new ConnectorWrapper();
    
    public SmartHttpConnection(String host, String xid, String authorization, SmartExecutorService executorService) {
        this.host = host;
        this.xid = xid;
        this.authorization = authorization;
        this.executorService = executorService;
    }

    public SmartHttpConnection(String host, String xid, String authorization) {
        this(host, xid, authorization, new SmartExecutorServiceImpl());
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, String xid) {
        this(host, xid, "Basic " + Base64.encode(tenant + "/" + username + ":" + password), new SmartExecutorServiceImpl());
    }
    
    public SmartHttpConnection(String host, String tenant, String username,
            String password, String xid, SmartExecutorService executorService) {
        this(host, "Basic " + Base64.encode(tenant + "/" + username + ":" + password), xid, executorService);
    }

    public void setConnectorWrapper(ConnectorWrapper connectorWrapper) {
        this.connectorWrapper = connectorWrapper;
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

            isBootstrapping = true; //setting to true in order for the writeHeaders method to not add the X-Id header
            response = executeRequest(new SmartRequestImpl(SmartConnection.BOOTSTRAP_REQUEST_CODE, id));
            isBootstrapping = false;
            
            if (response != null) {
                responseRow = response.getRow(0);
            }
        } while(response == null || responseRow == null || responseRow.getMessageId() != SmartConnection.BOOTSTRAP_RESPONSE_CODE);
        String[] data = responseRow.getData();
        authorization = "Basic " + Base64.encode(data[1] + "/" + data[2] + ":" + data[3]);
        return authorization;
    }
    
    public String templateRegistration(String templates) throws SDKException {
        SmartResponse respCheckRegistration = executeRequest(new SmartRequestImpl(SmartConnection.SMARTREST_API_PATH, ""));
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
            throw new SDKException("I/O error!", e);
        } finally {
            closeConnection();
        }
    }
    
    protected SmartResponse executeLongPollingRequest(SmartRequest request, boolean startHeartBeatWatcher) {
        try {
            if (startHeartBeatWatcher) {
                return openConnection(request.getPath())
                        .writeCommand()
                        .writeHeaders(request)
                        .writeBody(request)
                        .startHeartBeatWatcher()
                        .buildResponse();
            } else {
                return openConnection(request.getPath())
                        .writeCommand()
                        .writeHeaders(request)
                        .writeBody(request)
                        .buildResponse();
            }
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
    
    public SmartResponse executeLongPollingRequest(SmartRequest request) {
        return executeLongPollingRequest(request, true);
    }
    
    public void executeRequestAsync(SmartRequest request, SmartResponseEvaluator evaluator) {
        executorService.execute(new SmartRequestAsyncRunner(this, request, evaluator));
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
            connection = connectorWrapper.open(url, mode, timeout);
        } else {
            connection = connectorWrapper.open(url);
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
        if (! isBootstrapping) {
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
