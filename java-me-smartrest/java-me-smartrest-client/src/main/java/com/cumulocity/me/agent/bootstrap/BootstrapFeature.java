package com.cumulocity.me.agent.bootstrap;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;
import com.cumulocity.me.agent.plugin.impl.InternalAgentInfo;

public class BootstrapFeature extends BaseFeature{

    private BootstrapManager manager;
    
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        manager = new BootstrapManager(agentApi.getConfigurationService(), agentApi.getExternalIdProvider());
        InternalAgentInfo agentInfo = new InternalAgentInfo();
        agentInfo.setBootstrapConnection(manager.bootstrap());
        agentInfo.setCredentials(manager.getCredentials());
        agentApi.setAgentInfo(agentInfo);
    }
}
