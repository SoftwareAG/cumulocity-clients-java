package com.cumulocity.microservice.customencoders.api.exception;

import com.cumulocity.microservice.customencoders.api.model.EncoderResult;
import lombok.Getter;

public class EncoderServiceException extends Exception {

    @Getter
    protected EncoderResult result;

    public EncoderServiceException(Throwable throwable, String message, EncoderResult result) {
        super(message, throwable);
        this.result = result.setAsFailed(message);
    }
}
