package com.cumulocity.me.agent.integration;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.integration.impl.IntegrationHandler;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public class IntegrationFeature extends BaseFeature{
    private IntegrationHandler handler;

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        handler = new IntegrationHandler(agentApi.getExternalIdProvider(), agentApi.getInternalAgentInfo().getBootstrapConnection(), agentApi.getConfigurationService());
        agentApi.getInternalAgentInfo().setDeviceId(handler.integrate());
    }
}
