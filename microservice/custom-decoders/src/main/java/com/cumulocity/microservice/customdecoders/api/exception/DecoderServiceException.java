package com.cumulocity.microservice.customdecoders.api.exception;

import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import lombok.Getter;

public class DecoderServiceException extends Exception {

    @Getter
    protected DecoderResult result;

    public DecoderServiceException(Throwable throwable, String message, DecoderResult result) {
        super(message, throwable);
        this.result = result.setAsFailed(message);
    }
}
