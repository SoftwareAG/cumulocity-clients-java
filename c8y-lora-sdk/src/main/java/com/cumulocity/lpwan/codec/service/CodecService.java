/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.Decoder;
import com.cumulocity.lpwan.codec.exception.DecoderException;
import com.cumulocity.lpwan.codec.model.DecoderInput;
import com.cumulocity.lpwan.codec.model.DecoderOutput;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@TenantScope
public class CodecService {

    @Autowired
    private Decoder decoder;

    @Autowired
    private InventoryApi inventoryApi;

    /**
     * This method should decode the payload received by a particular device.
     *
     * @param payload
     * @return DecodedData
     */
    public DecoderOutput decode(DecoderInput payload) throws DecoderException {
        log.debug("Forwarding decoding request for the device with Id {} with payload {}", payload.getDeviceMoId(), payload.getPayload());
        return decoder.decode(payload);
    }
}
