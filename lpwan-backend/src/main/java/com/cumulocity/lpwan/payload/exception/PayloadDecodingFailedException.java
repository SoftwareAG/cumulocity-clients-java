package com.cumulocity.lpwan.payload.exception;

public class PayloadDecodingFailedException extends Exception {

    public PayloadDecodingFailedException(String message) {
        super(message);
    }
    public PayloadDecodingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
