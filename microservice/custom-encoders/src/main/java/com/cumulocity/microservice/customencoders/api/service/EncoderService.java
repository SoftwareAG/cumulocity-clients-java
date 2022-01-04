package com.cumulocity.microservice.customencoders.api.service;

import com.cumulocity.microservice.customencoders.api.exception.EncoderServiceException;
import com.cumulocity.microservice.customencoders.api.model.EncoderInputData;
import com.cumulocity.microservice.customencoders.api.model.EncoderResult;

import java.util.Map;

public interface EncoderService {
    /**
     * Encodes the EncoderInput object into EncoderResult object
     *
     * @param encoderInputData the EncoderInputData object containing the source device id, command name, command data and the properties
     * @return EncoderResult the EncoderResult object that contains the encoded hexadecimal command and/or additional properties like fport
     * @throws EncoderServiceException
     */
    EncoderResult encode(EncoderInputData encoderInputData) throws EncoderServiceException;
}
