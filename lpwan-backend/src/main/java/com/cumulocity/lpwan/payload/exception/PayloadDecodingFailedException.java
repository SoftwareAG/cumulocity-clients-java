package com.cumulocity.lpwan.payload.exception;

import java.text.MessageFormat;

public class PayloadDecodingFailedException extends Exception {

    public PayloadDecodingFailedException(String message) {
        super(message);
    }
    public PayloadDecodingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
