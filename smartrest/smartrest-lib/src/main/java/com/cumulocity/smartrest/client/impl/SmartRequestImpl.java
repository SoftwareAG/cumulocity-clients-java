package com.cumulocity.smartrest.client.impl;

import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartRequest;

public class SmartRequestImpl implements SmartRequest {

    private String path;
    private String data;
    
    public SmartRequestImpl(String data) {
        this.path = SmartConnection.SMARTREST_API_PATH;
        this.data = data;
    }
    
    public SmartRequestImpl(String path, String data) {
        this.path = path;
        this.data = data;
    }
    
    public SmartRequestImpl(int messageId, String data) {
        this.path = SmartConnection.SMARTREST_API_PATH;
        addMessageIdToBody(messageId, data);
    }
    
    public SmartRequestImpl(String path, int messageId, String data) {
        this.path = path;
        addMessageIdToBody(messageId, data);
    }
    
    public String getPath() {
        return path;
    }
    
    public String getData() {
        return data;
    }
    
    private void addMessageIdToBody(int messageId, String data) {
        this.data = messageId + "," + data;
    }
}
