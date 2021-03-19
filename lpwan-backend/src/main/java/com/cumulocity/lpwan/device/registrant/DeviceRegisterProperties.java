package com.cumulocity.lpwan.device.registrant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceRegisterProperties {

    @NotNull
    protected DeviceType deviceType;

    @Null
    private String uplinkCallback;

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
