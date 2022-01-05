/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.customencoders.api.model;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Data
public class EncoderResult {
    private String encodedCommand;
    private Map<String, String> properties;

    private String message;

    private boolean success = true;

    public final EncoderResult setAsFailed(String message) {
        success = false;
        this.message = message;
        return this;
    }

    public static EncoderResult empty() {
        return new EncoderResult();
    }
}
