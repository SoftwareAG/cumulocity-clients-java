package com.cumulocity.me.concurrent.exception;

public class ExecutionException extends Exception{
    private final Throwable cause;

    public ExecutionException(Throwable cause) {
        super("Future execution failed due to: " + cause.getMessage());
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
