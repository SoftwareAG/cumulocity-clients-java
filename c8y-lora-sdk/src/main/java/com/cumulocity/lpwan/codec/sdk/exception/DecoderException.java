package com.cumulocity.lpwan.codec.sdk.exception;

public class DecoderException extends Exception {
    public DecoderException() {
        super();
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause){
        super(message, cause);
    }
}
