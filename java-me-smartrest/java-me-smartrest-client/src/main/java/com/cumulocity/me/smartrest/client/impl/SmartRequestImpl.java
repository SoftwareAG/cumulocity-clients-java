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

    public String toString() {
        return "SmartRequestImpl{" +
                "path='" + path + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmartRequestImpl that = (SmartRequestImpl) o;

        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return !(data != null ? !data.equals(that.data) : that.data != null);

    }

    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
