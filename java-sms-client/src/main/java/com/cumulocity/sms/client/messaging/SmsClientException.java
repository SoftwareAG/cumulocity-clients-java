package com.cumulocity.sms.client.messaging;

public class SmsClientException extends RuntimeException {

    public SmsClientException(String message) {
        super(message);
    }

    public SmsClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
