package com.cumulocity.microservice.customdecoders.api.exception;

import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;

public class InvalidInputDataException extends DecoderServiceException {

    public InvalidInputDataException(Throwable throwable, String message, DecoderResult result) {
        super(throwable, message, result);
    }
}
