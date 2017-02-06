package com.cumulocity.email.client.exception;

public class EmailClientException extends RuntimeException {

    private static final long serialVersionUID = -4348667863958547325L;

    public EmailClientException(String string) {
        super(string);
    }

    public EmailClientException(String string, Throwable t) {
        super(string + "\n" + t.getMessage());
    }

}
