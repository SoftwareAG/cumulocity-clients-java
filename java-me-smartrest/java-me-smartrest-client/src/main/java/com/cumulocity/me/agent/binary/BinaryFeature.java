package com.cumulocity.me.agent.binary;

import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public class BinaryFeature extends BaseFeature{
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);

        String baseUrl = agentApi.getConfigurationService().get(ConfigurationKey.CONNECTION_HOST_URL);
        String authHeader = agentApi.getInternalAgentInfo().getCredentials();

        BinaryService binaryService = new BinaryService(baseUrl, authHeader);
        agentApi.setBinaryService(binaryService);
    }
}
