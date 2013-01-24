package com.cumulocity.me.rest.validate;

public class RepresentationValidationException extends RuntimeException {

    private static final long serialVersionUID = 8033981854918843659L;

    
    public RepresentationValidationException() {
        super();
    }

    public RepresentationValidationException(String message) {
        super(message);
    }
}
