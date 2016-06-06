package com.cumulocity.me.agent.plugin;

import com.cumulocity.me.agent.provider.MidletInformationProvider;

public abstract class AgentInfo {
    protected String deviceId;
    protected MidletInformationProvider midletInformationProvider;

    public AgentInfo() {
    }

    public AgentInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getAgentName() {
        return midletInformationProvider.getName();
    }

    public String getAgentVersion() {
        return midletInformationProvider.getVersion();
    }

    public String getAgentUrl() {
        return midletInformationProvider.getUrl();
    }

    public String getMidletInfo(String key) {
        return midletInformationProvider.get(key);
    }
}
