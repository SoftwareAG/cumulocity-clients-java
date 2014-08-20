package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;

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
    
    public String getPath() {
        return path;
    }
    
    public String getData() {
        return data;
    }
}
