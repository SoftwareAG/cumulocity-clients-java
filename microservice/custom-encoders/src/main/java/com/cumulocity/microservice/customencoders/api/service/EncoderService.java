package com.cumulocity.microservice.customencoders.api.service;

import com.cumulocity.microservice.customencoders.api.exception.EncoderServiceException;
import com.cumulocity.microservice.customencoders.api.model.EncoderInputData;
import com.cumulocity.microservice.customencoders.api.model.EncoderResult;

import java.util.Map;

public interface EncoderService {
    EncoderResult encode(EncoderInputData encoderInputData) throws EncoderServiceException;
}
