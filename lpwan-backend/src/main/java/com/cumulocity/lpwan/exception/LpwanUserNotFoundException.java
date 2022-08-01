package com.cumulocity.lpwan.exception;

public class LpwanUserNotFoundException extends LpwanServiceException {
    public LpwanUserNotFoundException(String message) {
        super(message);
    }

    public LpwanUserNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
