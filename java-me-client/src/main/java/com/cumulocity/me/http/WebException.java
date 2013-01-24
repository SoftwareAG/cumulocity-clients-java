package com.cumulocity.me.http;

public class WebException extends RuntimeException {

    private static final long serialVersionUID = -7800435096055893900L;
    private final WebResponse response;
    
    public WebException(WebResponse response) {
        this(response, null);
    }

    public WebException(WebResponse response, String message) {
        super(message);
        this.response = response;
    }
    
    public WebResponse getResponse() {
        return response;
    }
    
    public String getMessage() {
        String msg = "The server returned " + response.getStatus() + ": " + response.getMessage() + "! ";
        String superMsg = super.getMessage();
        if (superMsg != null) {
            msg = msg + superMsg;
        }
        return msg;
    }
}
