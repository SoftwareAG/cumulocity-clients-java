/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.rest;

import com.cumulocity.lpwan.codec.decoder.model.DecoderInput;
import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;
import com.cumulocity.lpwan.codec.exception.DecoderException;
import com.cumulocity.lpwan.codec.service.CodecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Objects;


@RestController
@Slf4j
public class CodecController {

    @Autowired
    private CodecService codecService;

    /**
     * This REST API should expose '/decode' endpoint
     *
     * @param input
     * @return DecodeResponse
     */
    @PostMapping(value = "/decode", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @NotNull DecoderOutput decode(@RequestBody @NotNull DecoderInput input) throws DecoderException {
        if(!Objects.isNull(input)) {
            log.debug("Received decode request for the device {}", input.getDeviceMoId());
        }
        else {
            log.debug("Received decode request with empty input.");
        }

        return codecService.decode(input);
    }
}
