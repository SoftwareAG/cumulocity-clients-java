package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;

public class SmartCometClient {

    public static final int SMARTREST_HANDSHAKE_CODE = 80;
    public static final int SMARTREST_SUBSCRIBE_CODE = 81;
    public static final int SMARTREST_UNSUBSCRIBE_CODE = 82;
    public static final int SMARTREST_CONNECT_CODE = 83;
    public static final int SMARTREST_DISCONNECT_CODE = 84;
    public static final int SMARTREST_ADVICE_CODE = 86;
    
    private final SmartConnection connection;
    
    private final SmartResponseEvaluator evaluator;
    
    private String clientId;
    
    private String path;
    
    private String[] channels;
    
    private boolean fixedSettings = false;
    
    private long timeout;
    private long interval;
    // 0: do nothing; 1: do handshake; 2: do connect
    private int reconnectAdvice;
    
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator) {
        this.clientId = null;
        this.evaluator = evaluator;
        this.connection = connection;
        this.timeout = 60000;
        this.interval = 0;
        this.reconnectAdvice = 2;
    }
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator, long timeout, long interval) {
        this.clientId = null;
        this.evaluator = evaluator;
        this.connection = connection;
        this.timeout = timeout;
        this.interval = interval;
        this.reconnectAdvice = 2;
    }
    
    public void startListenTo(String path, String[] channels) {
        if (path == null || channels == null || channels.length == 0) {
            throw new SDKException("Either there was no path or channel specified to listen to");
        }
        this.path = path;
        if (clientId == null) {
            clientId = handshake();
        }
        this.channels = channels;
        System.out.println("before subscribe");
        subscribe();
        System.out.println("before");
        SmartLongPolling longPolling = new SmartLongPolling(this);
        longPolling.start();      
        System.out.println("after");
    }

    public void stopListenTo() {
        if (path == null) {
            throw new SDKException("Nothing to stop");
        }
        if (clientId == null) {
            return;
        }
        this.reconnectAdvice = 0;
        disconnect();
    }
    
    public String handshake() {
        SmartRequestImpl request = new SmartRequestImpl(path, Integer.toString(SMARTREST_HANDSHAKE_CODE));
        SmartResponse response = connection.executeRequest(request);
        SmartRow[] responseLines = response.getDataRows();
        SmartRow responseLine = extractAdvice(responseLines)[0];
        if (responseLine.getMessageId() == 0) {
            this.clientId = responseLine.getData()[0];
        } else {
            throw new SDKException(responseLine);
        }
        return clientId;
    }
       
    public void subscribe() {
        SmartRequestImpl request = new SmartRequestImpl(path, buildSubscriptionBody(channels, SMARTREST_SUBSCRIBE_CODE));
        executeWithoutResponse(request);
    }
    
    public void unsubscribe() {
        SmartRequestImpl request = new SmartRequestImpl(path, buildSubscriptionBody(channels, SMARTREST_UNSUBSCRIBE_CODE));
        executeWithoutResponse(request);
    }
   
    public void connect() {
        SmartRequestImpl request = new SmartRequestImpl(path, SMARTREST_CONNECT_CODE + "," + clientId);
        final SmartResponse response = connection.executeRequest(request);
        if (response == null || response.isTimeout()) {
            return;
        } else if (!response.isSuccessful()){
            return;
        }
        final SmartRow[] rows = extractAdvice(response.getDataRows());
        Thread thread = new Thread(new Runnable() {
            public void run() {
                evaluator.evaluate(new SmartResponseImpl(response.getStatus(), response.getMessage(), rows));
            }
        });
        thread.start();
        
    }
    
    public void useFixedSettings(long timeout, long interval) {
        this.timeout = timeout;
        this.interval = interval;
        this.fixedSettings = true;
    }
    
    public void cleanUp() {
        this.connection.closeConnection();
    }
    
    public String getPath() {
        return path;
    }

    public String[] getChannels() {
        return channels;
    }
    
    public long getTimeout() {
        return timeout;
    }

    public long getInterval() {
        return interval;
    }

    public int getReconnectAdvice() {
        return reconnectAdvice;
    }
    
    private void disconnect() {
        SmartRequestImpl request = new SmartRequestImpl(path, SMARTREST_DISCONNECT_CODE + "," + clientId);
        executeWithoutResponse(request);
    }
    
    private void executeWithoutResponse(SmartRequest request) {
        SmartResponse response = connection.executeRequest(request);
        SmartRow[] responseLines = response.getDataRows();
        if (responseLines == null || responseLines.length == 0) {
            return;
        } else {
            responseLines = extractAdvice(responseLines);
            if (responseLines != null) {
                throw new SDKException(responseLines); 
            }
        }
    }
        
    private String buildSubscriptionBody(String[] channels, int code) {
        String subscribeBody =  code + "," + clientId + "," + channels[0];
        for(int i = 1; i < channels.length; i++) {
            subscribeBody = subscribeBody + "\n" + code + "," + clientId + "," + channels[i];
        }
        return subscribeBody;
    }
    
    private SmartRow[] extractAdvice(SmartRow[] rows) {
        int index = -1;
        for(int i = 0; i < rows.length; i++) {
            if (rows[i].getMessageId() == SMARTREST_ADVICE_CODE) {
                index = i;
            }
        }
        if (index == -1) {
            return rows;
        }
        if (rows.length == 1) {
            return null;
        }
        setAdvice(rows[index]);
        SmartRow[] newRows = new SmartRow[rows.length - 1];
        System.arraycopy(rows, 0, newRows, 0, index);
        System.arraycopy(rows, index + 1, newRows, index, newRows.length - index);
        return newRows;
    }
    
    private void setAdvice(SmartRow adviceRow) {
        String timeout;
        String interval;
        String reconnectAdvice;
        String[] data = adviceRow.getData();
        if (!fixedSettings) {
            if (!(timeout = data[0]).equals("")) {
                this.timeout = Long.parseLong(timeout);
            }
            if (!(interval = data[1]).equals("")) {
                this.interval = Long.parseLong(interval);
            }
        }
        if (!(reconnectAdvice = data[2]).equals("")) {
            if (reconnectAdvice.equals("handshake")) {
                this.reconnectAdvice = 1;
            } else if (reconnectAdvice.equals("retry")) {
                this.reconnectAdvice = 2;
            } else {
                this.reconnectAdvice = 0;
            }
        }
    }
}
