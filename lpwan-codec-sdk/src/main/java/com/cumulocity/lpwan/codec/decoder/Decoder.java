/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder;

import com.cumulocity.lpwan.codec.decoder.exception.DecoderException;
import com.cumulocity.lpwan.codec.decoder.model.DecoderInput;
import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;

import javax.validation.constraints.NotNull;

/**
 * the <b>Decoder</b> interface will be implemented by a codec microservice to provide the decoding logic.
 *
 * @author Bhaskar Reddy Byreddy
 * @author Atul Kumar Panda
 * @version 1.0
 * @since 2021 -12-01
 */
public interface Decoder {
    /**
     * This method should provide a decoding logic for the payload received and generate a <b>DecoderOutput</b> object.
     *
     * @param input A non-null input parameter that is carries the payload to be decoded along with other supporting elements.
     * @return DecoderOutput
     * @throws DecoderException
     * @see DecoderException
     */
    @NotNull DecoderOutput decode(@NotNull DecoderInput input) throws DecoderException;
}
