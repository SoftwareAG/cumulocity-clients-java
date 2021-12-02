/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec;

import com.cumulocity.lpwan.codec.model.DeviceInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The <b>Codec</b> interface exposes methods to provide the uniquely supported devices and the context path.
 *
 * @author Bhaskar Reddy Byreddy
 * @author Atul Kumar Panda
 * @version 1.0
 * @since 2021-12-01
 */
public interface Codec {

    /**
     * This method returns a set of uniquely supported devices w.r.t the device manufacturer and the device model.
     *
     * @return Set<DeviceInfo> set
     */
    @NotNull @NotEmpty Set<DeviceInfo> supportsDevices();

    /**
     * This method returns the context-path of the codec microservice.
     *
     * @return String codec microservice context-path
     */
    @NotBlank String getMicroserviceContextPath();
}
