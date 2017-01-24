package com.cumulocity.exportroute.credentials.exception;

public class DecryptFailedException extends Exception {

    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, IllegalStateException cause) {
        super(message, cause);
    }
}
