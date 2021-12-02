/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>DeviceInfo</b> class uniquely represents one device with the device manufacturer name, the device model.
 *
 * @author Bhaskar Reddy Byreddy
 * @author Atul Kumar Panda
 * @version 1.0
 * @since 2021 -12-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {
    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    /**
     * This method checks if the fields are null or empty.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
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
