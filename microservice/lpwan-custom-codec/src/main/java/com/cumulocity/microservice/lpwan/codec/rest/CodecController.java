/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.rest;

import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.exception.InvalidInputDataException;
import com.cumulocity.microservice.customdecoders.api.model.DecoderInputData;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.customdecoders.api.service.DecoderService;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.model.idtype.GId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * The <b>CodecController</b> is a rest controller that defines the endpoints.
 */
@RestController
@Slf4j
public class CodecController {

    @Autowired
    private DecoderService decoderService;

    /**
     * This REST API should expose '/decode' endpoint
     *
     * @param inputData A non-null input parameter that is carries the payload to be decoded along with other supporting elements.
     * @return DecoderResult represents the output that carries the measurement(s)/event(s)/alarm(s) to be created and/or the managed object properties to be updated.
     * @throws DecoderServiceException
     * @see DecoderServiceException {@link com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException}
     */
    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull DecoderResult decode(@RequestBody @NotNull DecoderInputData inputData) throws DecoderServiceException {
        try {
            if(Objects.isNull(inputData)) {
                throw new IllegalArgumentException("Decoder is invoked with null input data.");
            }
            else {
                log.debug("Decoder is invoked with the following input data \\n {}", inputData);
            }

            LpwanDecoderInputData lpwanDecoderInputData = new LpwanDecoderInputData(inputData.getValue(), GId.asGId(inputData.getSourceDeviceId()), inputData.getArgs());
            return decoderService.decode(lpwanDecoderInputData.getValue(), GId.asGId(lpwanDecoderInputData.getSourceDeviceId()), lpwanDecoderInputData.getArgs());
        } catch (IllegalArgumentException e) {
            log.error("Decoder failed as it received invalid input.", e);
            throw new InvalidInputDataException(e, e.getMessage(), DecoderResult.empty());
        }
    }
}
