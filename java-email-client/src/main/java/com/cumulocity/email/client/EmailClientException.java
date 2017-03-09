package com.cumulocity.email.client;

public class EmailClientException extends RuntimeException {

    public EmailClientException(String string, Throwable t) {
        super(string + "\n" + t.getMessage());
    }
}
