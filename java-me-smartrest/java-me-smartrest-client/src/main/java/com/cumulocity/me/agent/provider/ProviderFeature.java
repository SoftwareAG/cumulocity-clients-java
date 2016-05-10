package com.cumulocity.me.agent.provider;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

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
