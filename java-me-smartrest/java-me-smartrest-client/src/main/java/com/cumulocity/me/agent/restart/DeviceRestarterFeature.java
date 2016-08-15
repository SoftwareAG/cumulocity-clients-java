package com.cumulocity.me.agent.restart;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public class DeviceRestarterFeature extends BaseFeature{
    private final DeviceRestarter restarter;

    public DeviceRestarterFeature(DeviceRestarter restarter) {
        this.restarter = restarter;
    }

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        agentApi.setDeviceRestarter(restarter);
    }
}
