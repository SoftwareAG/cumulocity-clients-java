package com.cumulocity.microservice.exception;

import com.cumulocity.sdk.client.SDKException;

public class AgentException extends RuntimeException {

    public AgentException(String msg) {
        super(msg);
    }

    public AgentException(String msg, SDKException e) {
        super(msg, e);
    }

}
