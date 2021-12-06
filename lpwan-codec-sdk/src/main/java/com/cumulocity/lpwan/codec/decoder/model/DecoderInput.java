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
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecoderInput {

    /**
     * The deviceMoId represents the managed object id of the device.
     */
    @NotBlank
    private String deviceMoId;

    /**
     * The deviceInfo contains the information about the device such as Manufacturer or model of the device.
     */
    @NotNull
    private DeviceInfo deviceInfo;

    /**
     * The deviceEui represents the EUI (or the external id) of teh device.
     */
    @NotBlank
    private String deviceEui;

    /**
     * The fPort represents the port number received as an input.
     */
    private Integer fPort;

    /**
     * The payload represents the actual payload data as a HEX string.
     */
    @NotBlank
    private String payload;

    /**
     * The updateTime represents the Date and Time when the payload is sent.
     */
    @NotNull
    private Long updateTime;

    /**
     * This method converts the device managed object id to a <b>GId</b>.
     *
     * @return GId the device managed object id as <b>GId</b>
     * @see GId
     */
    public GId getDeviceMoIdAsGId() {
        return GId.asGId(deviceMoId);
    }

    /**
     * This method validates the object fields.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     *
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