/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk;

public class SDKException extends RuntimeException {

    private static final long serialVersionUID = -7264072363017210113L;
    
    private int httpStatusCode = -1;
    
    public SDKException(String string) {
        super(string);
    }

    public SDKException(String string, Throwable t) {
        super(string + "\n" + t.getMessage());
    }

    public SDKException(int httpStatusCode, String string) {
        super(string);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatus() {
        return httpStatusCode;
    }

}
