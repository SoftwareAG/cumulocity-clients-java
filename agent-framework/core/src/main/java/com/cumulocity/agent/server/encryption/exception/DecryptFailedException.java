package com.cumulocity.agent.server.encryption.exception;

public class DecryptFailedException extends Exception {

    private static final long serialVersionUID = 4223229338553714990L;

    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, IllegalStateException cause) {
        super(message, cause);
    }
}
