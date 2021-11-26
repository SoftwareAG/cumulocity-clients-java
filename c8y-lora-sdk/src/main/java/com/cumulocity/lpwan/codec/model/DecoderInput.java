/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The DecoderInput class represents the format nad content of the request coming in for decoding a device data where it must contain the payload data and the device id.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecoderInput {

    @NotNull
    private String deviceMoId;

    @NotNull
    private DeviceInfo deviceInfo;

    @NotNull
    private String deviceEui;

    private Integer fPort;

    @NotNull
    private String payload;

    @NotNull
    private Long updateTime;

    public void validate() {
        if (Strings.isNullOrEmpty(deviceMoId)) {
            throw new IllegalArgumentException("'deviceMoId' is a mandatory parameter.");
        }

        if (Strings.isNullOrEmpty(deviceEui)) {
            throw new IllegalArgumentException("'deviceEui' is a mandatory parameter.");
        }

        if (Strings.isNullOrEmpty(payload)) {
            throw new IllegalArgumentException("'payload' is a mandatory parameter.");
        }

        if (Objects.isNull(updateTime)) {
            throw new IllegalArgumentException("'updateTime' is a mandatory parameter.");
        }

        if (Objects.isNull(deviceInfo)) {
            throw new IllegalArgumentException("'deviceInfo' is a mandatory parameter.");
        }
        else {
            deviceInfo.validate();
        }
    }
}