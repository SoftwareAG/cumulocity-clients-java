package com.cumulocity.lpwan.codec.exception;

public class LpwanCodecServiceException extends Exception {
    public LpwanCodecServiceException(String message) {
        super(message);
    }

    public LpwanCodecServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
