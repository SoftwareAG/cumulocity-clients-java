package com.cumulocity.microservice.exception;

public class AgentInvalidDataException extends RuntimeException {
    
    public AgentInvalidDataException(String msg) {
        super(msg);
    }

    public AgentInvalidDataException(String msg, Exception e) {
        super(msg, e);
    }

}
