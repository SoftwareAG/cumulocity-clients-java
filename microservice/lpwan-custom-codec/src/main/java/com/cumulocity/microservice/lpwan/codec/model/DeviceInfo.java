/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>DeviceInfo</b> class uniquely represents one device with the device manufacturer name, the device model.
 *
 */
@Getter
@EqualsAndHashCode
public class DeviceInfo {

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    /**
     * Instantiates a new DeviceInfo.
     * @param manufacturer represents the name of the device manufacturer.
     * @param model        represents the name of the device model.
     */
    public DeviceInfo(@NotBlank String manufacturer, @NotBlank String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    /**
     * This method validates the object field.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    public void validate() {
        List<String> missingParameters = new ArrayList<>(2);

        if (Strings.isNullOrEmpty(manufacturer)) {
            missingParameters.add("'manufacturer'");
        }

        if (Strings.isNullOrEmpty(model)) {
            missingParameters.add("'model'");
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
