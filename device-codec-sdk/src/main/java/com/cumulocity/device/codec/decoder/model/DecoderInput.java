/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.device.codec.decoder.model;

import com.cumulocity.device.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The DecoderInput class represents the format nad content of the request coming in for decoding a device data where it must contain the payload data and the device id.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecoderInput {

    @NotBlank
    private String deviceMoId;

    @NotNull
    private DeviceInfo deviceInfo;

    @NotBlank
    private String deviceEui;

    private Integer fPort;

    @NotBlank
    private String payload;

    @NotNull
    private Long updateTime;

    public GId getDeviceMoIdAsGId() {
        return GId.asGId(deviceMoId);
    }

    public void validate() {
        List<String> missingParameters = new ArrayList<>(5);

        if (Strings.isNullOrEmpty(deviceMoId)) {
            missingParameters.add("'deviceMoId'");
        }

        if (Strings.isNullOrEmpty(deviceEui)) {
            missingParameters.add("'deviceEui'");
        }

        if (Strings.isNullOrEmpty(payload)) {
            missingParameters.add("'payload'");
        }

        if (Objects.isNull(updateTime)) {
            missingParameters.add("'updateTime'");
        }

        if (Objects.isNull(deviceInfo)) {
            missingParameters.add("'deviceInfo'");
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DecoderInput is missing mandatory parameters: " + String.join(", ", missingParameters));
        }

        deviceInfo.validate();
    }
}