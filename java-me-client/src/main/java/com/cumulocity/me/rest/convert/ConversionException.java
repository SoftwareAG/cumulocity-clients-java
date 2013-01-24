package com.cumulocity.me.rest.convert;

public class ConversionException extends RuntimeException {

    private static final long serialVersionUID = 7858550407716749160L;

    public ConversionException() {
        super();
    }

    public ConversionException(String message) {
        super(message);
    }
}
