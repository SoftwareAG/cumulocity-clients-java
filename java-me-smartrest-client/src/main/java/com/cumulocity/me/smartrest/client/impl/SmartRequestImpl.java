package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.smartrest.client.SmartRequest;

public class SmartRequestImpl implements SmartRequest {

    private byte[] data;
    
    public SmartRequestImpl(byte[] data) {
        this.data = data;
    }
    
    public byte[] getData() {
        return data;
    }
}
