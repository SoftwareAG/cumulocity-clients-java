/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client;

public class SDKException extends Exception {

    private static final long serialVersionUID = 2723464890693892731L;

    private int httpStatusCode = -1;

    public SDKException(String string) {
        super(string);
    }

    public SDKException(String string, Throwable t) {
        super(string, t);
    }

    public SDKException(int httpStatusCode, String string) {
        super(string);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatus() {
        return httpStatusCode;
    }

}
