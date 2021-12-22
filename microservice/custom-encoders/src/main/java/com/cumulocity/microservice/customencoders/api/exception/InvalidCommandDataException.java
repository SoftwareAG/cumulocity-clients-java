package com.cumulocity.microservice.customencoders.api.exception;


import com.cumulocity.microservice.customencoders.api.model.EncoderResult;

public class InvalidCommandDataException extends EncoderServiceException {

    public InvalidCommandDataException(Throwable throwable, String message, EncoderResult result) {
        super(throwable, message, result);
    }
}
