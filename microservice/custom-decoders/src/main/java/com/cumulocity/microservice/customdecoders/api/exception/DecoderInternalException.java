package com.cumulocity.microservice.customdecoders.api.exception;

import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;

public class DecoderInternalException extends DecoderServiceException {

    public DecoderInternalException(Throwable throwable, String message, DecoderResult result) {
        super(throwable, message, result);
    }
}
