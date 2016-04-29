package com.cumulocity.me.agent.integration;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.feature.InternalAgentApi;

public class DeviceUpdateFeature extends BaseFeature{
    private DeviceUpdateHandler handler;

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        this.handler = new DeviceUpdateHandler(agentApi);
    }
    
    public void start() {
        handler.updateDevice();
    }
    
}
