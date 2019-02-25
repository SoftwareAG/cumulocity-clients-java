package com.cumulocity.microservice.customdecoders.api.service;

import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.model.idtype.GId;

import java.util.Map;

public interface DecoderService {

    String ARG_RESOURCE_PATH = "resourcePath";

    /**
     * Decodes byte array data into DecoderResult object.
     *
     * @param inputData Hex encoded input byte array
     * @param deviceId device from which this data comes from
     * @param args additional arguments that may be required by decoder
     * @return DecoderResult object
     */
    DecoderResult decode(String inputData, GId deviceId, Map<String, String> args) throws DecoderServiceException;
}
