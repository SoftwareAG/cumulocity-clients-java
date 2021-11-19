/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * The DecodeRequest class represents the format nad content of the request coming in for decoding a device data where it must contain the payload data and the device id.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecodePayload {

    @NotNull
    private String deviceMoId;

    @NotNull
    private String deviceEui;

    private int fPort;

    @NonNull
    private String payload;

    @NotNull
    private Long updateTime;
}