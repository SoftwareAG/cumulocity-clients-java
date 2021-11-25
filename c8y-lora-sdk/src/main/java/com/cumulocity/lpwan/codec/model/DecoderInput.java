/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.cumulocity.lpwan.codec.exception.DecoderException;
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

    public void validate() throws DecoderException {
        if (Objects.isNull(deviceMoId)) {
            throw new DecoderException("Device Managed Object ID is a mandatory parameter.");
        }

        if (Objects.isNull(deviceEui)) {
            throw new DecoderException("Device EUI is a mandatory parameter.");
        }

        if (Objects.isNull(payload)) {
            throw new DecoderException("Payload is a mandatory parameter.");
        }

        if (Objects.isNull(updateTime)) {
            throw new DecoderException("Update Time is a mandatory parameter.");
        }

        if (Objects.isNull(deviceInfo) || !deviceInfo.isValid()) {
            throw new DecoderException("Device manufacturer and model are mandatory parameters.");
        }
    }
}