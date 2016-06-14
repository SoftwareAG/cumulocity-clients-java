package com.cumulocity.me.agent.smartrest.model;

import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;

public class RequestBufferItem {
    private final String xId;
    private final SmartRequest request;
    private final SmartResponseEvaluator callback;
    
    private int lineNumber;

    public RequestBufferItem(String xId, SmartRequest request, SmartResponseEvaluator callback) {
        this.request = request;
        this.callback = callback;
        this.xId = xId;
    }

    public SmartRequest getRequest() {
        return request;
    }

    public SmartResponseEvaluator getCallback() {
        return callback;
    }

    public String getXId() {
        return xId;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
