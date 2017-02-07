package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.concurrent.impl.SmartExecutorServiceImpl;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.concurrent.SmartExecutorService;
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
    public static final int SMARTREST_RELIABLE_COUNT_CODE = 88;
    
    private final SmartConnection connection;    
    private final SmartResponseEvaluator evaluator;    
    private String clientId;    
    private String path;    
    private String[] channels;    
    private boolean fixedSettings = false;
    private final SmartExecutorService executorService;    
    private volatile SmartLongPolling longPolling;    
    private long interval;
    // 0: do nothing; 1: do handshake; 2: do connect
    private int reconnectAdvice;
    private boolean reliable;
    private int reliableRequestNumber = -1;
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator, long interval, SmartExecutorService executorService) {
        this.clientId = null;
        this.evaluator = evaluator;
        this.connection = connection;
        this.interval = interval;
        this.reconnectAdvice = 2;
        this.executorService = executorService;
    }
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator, long interval) {
        this(connection, evaluator, interval, new SmartExecutorServiceImpl());
    }
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator, SmartExecutorService executorService) {
        this(connection, evaluator, 0, executorService);
    }
    
    public SmartCometClient(SmartConnection connection, SmartResponseEvaluator evaluator) {
        this(connection, evaluator, 0, new SmartExecutorServiceImpl());
    }
    
    public void startListenTo(String path, String[] channels) {
        if (path == null || channels == null || channels.length == 0) {
            throw new SDKException("Either there was no path or channel specified to listen to");
        }
        if (longPolling == null) {
            this.path = path;
            if (clientId == null) {
                clientId = handshake();
            }
            if (clientId == null ) {
                throw new SDKException("Unauthorized");
            }
            this.channels = channels;
            subscribe();
            longPolling = new SmartLongPolling(this);
            executorService.execute(longPolling);   
        } else {
            throw new SDKException("SmartCometClient already started");
        }
    }

    public void stopListenTo() {
        if (path == null) {
            return;
        }
        if (clientId == null) {
            return;
        }
        this.reconnectAdvice = 0;
        disconnect();
    }
    
    protected String handshake() {
        SmartRequestImpl request;
        if (isReliable()) {
            request = new SmartRequestImpl(path, Integer.toString(SMARTREST_HANDSHAKE_CODE) + ",true");
        } else {
            request = new SmartRequestImpl(path, Integer.toString(SMARTREST_HANDSHAKE_CODE));
        }
        SmartResponse response = connection.executeRequest(request);
        if (!response.isSuccessful()) {
            return null;
        }
        SmartRow[] responseLines = response.getDataRows();
        SmartRow responseLine = extractAdvice(responseLines)[0];
        if (responseLine.getMessageId() == 0) {
            this.clientId = responseLine.getData(0);
        } else {
            throw new SDKException(responseLine);
        }
        return clientId;
    }
       
    protected void subscribe() {
        SmartRequestImpl request = new SmartRequestImpl(path, buildSubscriptionBody(channels, SMARTREST_SUBSCRIBE_CODE));
        final SmartResponse response = connection.executeRequest(request);
        if (response == null || response.isTimeout()) {
            return;
        } else if (!response.isSuccessful()){
            return;
        }
        extractAdvice(response.getDataRows());
    }
    
    protected void unsubscribe() {
        SmartRequestImpl request = new SmartRequestImpl(path, buildSubscriptionBody(channels, SMARTREST_UNSUBSCRIBE_CODE));
        executeWithoutResponse(request);
    }
   
    protected void connect() {
        SmartRequestImpl request;
        if (isReliable()) {
            request = new SmartRequestImpl(path, SMARTREST_CONNECT_CODE + "," + clientId + "," + reliableRequestNumber);
        } else {
            request = new SmartRequestImpl(path, SMARTREST_CONNECT_CODE + "," + clientId);
        }

        try {
            final SmartResponse response = connection.executeLongPollingRequest(request);
            if (response == null || response.isTimeout()) {
                return;
            } else if (!response.isSuccessful()){
                return;
            }
            SmartRow[] rows = extractAdvice(response.getDataRows());
            if (isReliable()) {
                rows = extractReliableNumber(rows);
            }
            final SmartRow[] finalRows = rows;
            executorService.execute(new Runnable() {
                public void run() {
                    evaluator.evaluate(new SmartResponseImpl(response.getStatus(), response.getMessage(), finalRows));
                }
            });
        } catch (SDKException e) {
            // reconnect
        } finally {
            connection.closeConnection();
        }
    }
    
    public void useFixedSettings(long interval) {
        this.interval = interval;
        this.fixedSettings = true;
    }
    
    protected void cleanUp() {
        this.connection.closeConnection();
    }
    
    public String getPath() {
        return path;
    }

    public String[] getChannels() {
        return channels;
    }

    public long getInterval() {
        return interval;
    }

    public synchronized int getReconnectAdvice() {
        return reconnectAdvice;
    }
    
    private void disconnect() {
        longPolling.setFlag(false);
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
        setAdvice(rows[index]);
        SmartRow[] newRows = new SmartRow[rows.length - 1];
        System.arraycopy(rows, 0, newRows, 0, index);
        System.arraycopy(rows, index + 1, newRows, index, newRows.length - index);
        return newRows;
    }
    
    private SmartRow[] extractReliableNumber(SmartRow[] rows){
        int index = -1;
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (SMARTREST_RELIABLE_COUNT_CODE == row.getMessageId()) {
                reliableRequestNumber = Integer.parseInt(row.getData(0));
                index = i;
                break;
            }
        }
        if (index == -1) {
            return rows;
        }
        SmartRow[] newRows = new SmartRow[rows.length - 1];
        System.arraycopy(rows, 0, newRows, 0, index);
        System.arraycopy(rows, index + 1, newRows, index, newRows.length - index);
        return newRows;
    }
    
    private void setAdvice(SmartRow adviceRow) {
        String interval;
        String reconnectAdvice;
        String[] data = adviceRow.getData();
        if (!fixedSettings) {
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

    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    public boolean isReliable() {
        return reliable;
    }
}