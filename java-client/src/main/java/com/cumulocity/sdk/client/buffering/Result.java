package com.cumulocity.sdk.client.buffering;


public class Result {

    private RuntimeException exception;
    private Object response;
    
    public RuntimeException getException() {
        return exception;
    }
    public void setException(RuntimeException exception) {
        this.exception = exception;
    }
    public Object getResponse() {
        return response;
    }
    public void setResponse(Object response) {
        this.response = response;
    }
    
}
