/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.device.registrant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRegisterProperties {

    @NotNull
    protected DeviceType deviceType;

    @Null
    private String uplinkCallback;

    @NotBlank
    private String lnsConnectionName;

    @JsonIgnore
    public DeviceType getDeviceType() {
        return deviceType;
    }

    @JsonProperty("deviceType")
    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getUplinkCallback() {
        return uplinkCallback;
    }

    public void setUplinkCallback(String uplinkCallback) {
        this.uplinkCallback = uplinkCallback;
    }

}
