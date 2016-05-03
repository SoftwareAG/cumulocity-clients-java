package com.cumulocity.me.agent.plugin;

public class AgentInfo {
    protected String deviceId;

    public AgentInfo() {
    }

    public AgentInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
