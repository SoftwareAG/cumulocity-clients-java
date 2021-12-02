/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder.model;

import com.cumulocity.lpwan.codec.model.DeviceInfo;
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
 * The <b>DecoderInput</b> class represents the format and content of the request coming in for decoding a device data.
 *
 *  @author Bhaskar Reddy Byreddy
 *  @author Atul Kumar Panda
 *  @version 1.0
 *  @since   2021-12-01
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

    /**
     * This method converts the device managed object id to a <b>GId</b>.
     *
     * @return GId the device managed object id as <b>GId</b>
     * @see GId.java
     */
    public GId getDeviceMoIdAsGId() {
        return GId.asGId(deviceMoId);
    }

    /**
     * This method checks if the fields are null or empty.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
     */
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