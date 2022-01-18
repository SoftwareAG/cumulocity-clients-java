/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.customdecoders.api.exception;

import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import lombok.Getter;

public class DecoderServiceException extends Exception {

    @Getter
    protected DecoderResult result;

    public DecoderServiceException(Throwable throwable, String message, DecoderResult result) {
        super(message, throwable);
        this.result = result.setAsFailed(message);
    }
}
