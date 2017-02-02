package com.cumulocity.java.sms.client.request;

public class SmsClientException extends RuntimeException {

    public SmsClientException() {
    }

    public SmsClientException(String message) {
        super(message);
    }

    public SmsClientException(Throwable cause) {
        super(cause);
    }

    public SmsClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
