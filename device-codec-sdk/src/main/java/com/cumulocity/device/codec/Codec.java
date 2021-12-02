/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.device.codec;

import com.cumulocity.device.codec.model.DeviceInfo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface Codec {

    /**
     * This method should return a set of uniquely supported devices w.r.t the device manufacturer and the device model.
     *
     * @return Set<DeviceInfo>
     */
    @NotNull @NotEmpty Set<DeviceInfo> supportsDevices();

    /**
     * @return
     */
    @NotNull @NotEmpty String getMicroserviceContextPath();
}
