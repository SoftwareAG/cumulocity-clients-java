/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.decoder.Decoder;
import com.cumulocity.lpwan.codec.decoder.exception.DecoderException;
import com.cumulocity.lpwan.codec.decoder.model.DecoderInput;
import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;
import com.cumulocity.microservice.context.inject.TenantScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The <b>CodecService</b> class delegates the decoding request to the <b>Decoder</b> implementer.
 */
@Service
@Slf4j
@TenantScope
public class CodecService {

    @Autowired
    private Decoder decoder;

    /**
     * This method delegates the decoding request to the <b>Decoder</b> implementer.
     *
     * @param payload the payload
     * @return DecodedData decoder output
     * @throws DecoderException
     * @see DecoderException {@link com.cumulocity.lpwan.codec.decoder.exception.DecoderException}
     */
    public @NotNull DecoderOutput decode(@NotNull DecoderInput payload) throws DecoderException {
        if(Objects.isNull(payload)) {
            throw new IllegalArgumentException("Decoder invoked with empty payload.");
        }
        payload.validate();

        log.debug("Forwarding decoding request for the device with Id {} with payload {}", payload.getDeviceMoId(), payload.getPayload());

        return decoder.decode(payload);
    }
}
