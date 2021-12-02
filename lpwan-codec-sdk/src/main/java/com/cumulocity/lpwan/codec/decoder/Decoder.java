/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder;

import com.cumulocity.lpwan.codec.decoder.model.DecoderInput;
import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;
import com.cumulocity.lpwan.codec.exception.DecoderException;

import javax.validation.constraints.NotNull;

/**
 * This interface will be implemented to provide a decoding logic.
 */
public interface Decoder {
    /**
     * This method should take the following params and should return the decoded response with mapping.
     * This method should provide a decoding logic w.r.t the device info.
     *
     * @param input - represents the payload object that is meant to be decoded.
     * @return DecodeResponse - represents the response that is formed after decoding the device payload.
     * @throws DecoderException - represents the custom exception thrown while decoding the payload.
     */
    @NotNull DecoderOutput decode(@NotNull DecoderInput input) throws DecoderException;
}
