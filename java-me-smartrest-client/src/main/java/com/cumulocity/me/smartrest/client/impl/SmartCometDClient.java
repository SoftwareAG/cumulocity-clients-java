package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartResponse;

public class SmartCometDClient {
    
    public static final String SMARTREST_HANDSHAKE_CODE = "80";
    public static final String SMARTREST_SUBSCRIBE_CODE = "81";
    public static final String SMARTREST_UNSUBSCRIBE_CODE = "82";
    public static final String SMARTREST_CONNECT_CODE = "83";
    public static final String SMARTREST_DISCONNECT_CODE = "84";
    public static final String SMARTREST_ADVICE_CODE = "86";
    
    private final SmartConnection connection;
    
    private String clientId;
    
    public SmartCometDClient(SmartConnection connection) {
        this.clientId = null;
        this.connection = connection;
    }
    
    public SmartResponse listenTo(String path, String[] channels) {
        if (clientId == null) {
            clientId = handshake(path);
        }
        System.out.println(clientId);
        return null;
    }
    
    private String handshake(String path) {
        SmartRequestImpl request = new SmartRequestImpl(path, SMARTREST_HANDSHAKE_CODE);
        SmartResponse response = connection.executeRequest(request);
        SmartRow responseLine = response.getDataRows()[0];
        if (responseLine.getMessageId() == 0) {
            this.clientId = responseLine.getData()[0];
        } else {
            throw new SDKException(responseLine);
        }
        return clientId;
    }
    
    /*
    private boolean subscribe(String[] endpoints) {
        
    }
    
    private boolean unsubscribe(String[] endpoints) {
        
    }
    
    private SmartResponse connect() {
        
    }
    
    private boolean disconnect() {
        
    }
    */
}
