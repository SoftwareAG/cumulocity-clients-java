package com.cumulocity.sdk.client.buffering;

import com.cumulocity.sdk.client.SDKException;


public class Result {

    private SDKException exception;
    private Object response;
    
    public SDKException getException() {
        return exception;
    }
    public void setException(SDKException exception) {
        this.exception = exception;
    }
    public Object getResponse() {
        return response;
    }
    public void setResponse(Object response) {
        this.response = response;
    }
    
}
