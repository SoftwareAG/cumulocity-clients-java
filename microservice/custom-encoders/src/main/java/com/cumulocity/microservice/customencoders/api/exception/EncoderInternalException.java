package com.cumulocity.microservice.customencoders.api.exception;

import com.cumulocity.microservice.customencoders.api.model.EncoderResult;

public class EncoderInternalException extends EncoderServiceException {

    public EncoderInternalException(Throwable throwable, String message, EncoderResult result) {
        super(throwable, message, result);
    }
}
