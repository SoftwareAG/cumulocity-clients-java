package com.cumulocity.me.concurrent.exception;

public class TimeoutException extends Exception{
    public TimeoutException() {
        super("Future execution did not finish before timout");
    }
}
