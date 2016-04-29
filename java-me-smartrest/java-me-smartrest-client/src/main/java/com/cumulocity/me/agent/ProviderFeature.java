package com.cumulocity.me.agent;

import com.cumulocity.me.agent.bootstrap.ExternalIdProvider;
import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.feature.InternalAgentApi;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;

public class ProviderFeature extends BaseFeature{
    private final ExternalIdProvider idProvider;
    private final DeviceInformationProvider deviceInfoProvider;

    public ProviderFeature(ExternalIdProvider idProvider, DeviceInformationProvider deviceInfoProvider) {
        this.idProvider = idProvider;
        this.deviceInfoProvider = deviceInfoProvider;
    }
    
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        agentApi.setExternalIdProvider(idProvider);
        agentApi.setDeviceInformationProvider(deviceInfoProvider);
    }
    
}
