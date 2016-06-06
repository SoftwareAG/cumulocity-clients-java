package com.cumulocity.me.agent.provider;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;
import com.cumulocity.me.agent.plugin.impl.InternalAgentInfo;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class ProviderFeature extends BaseFeature{
    private static final Logger LOG = LoggerFactory.getLogger(ProviderFeature.class);

    private final ExternalIdProvider idProvider;
    private final DeviceInformationProvider deviceInfoProvider;
    private final MidletInformationProvider midletInformationProvider;

    public ProviderFeature(ExternalIdProvider idProvider, DeviceInformationProvider deviceInfoProvider, MidletInformationProvider midletInformationProvider) {
        this.idProvider = idProvider;
        this.deviceInfoProvider = deviceInfoProvider;
        this.midletInformationProvider = midletInformationProvider;
    }
    
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        agentApi.setExternalIdProvider(idProvider);
        agentApi.setDeviceInformationProvider(deviceInfoProvider);
        ensureAgentInfoExists();
        InternalAgentInfo agentInfo = agentApi.getInternalAgentInfo();
        agentInfo.setMidletInfoProvider(midletInformationProvider);

        LOG.info("Starting agent software");
        LOG.info("name: " + agentInfo.getAgentName());
        LOG.info("version: " + agentInfo.getAgentVersion());
        LOG.info("url: " + agentInfo.getAgentUrl());
    }

    private void ensureAgentInfoExists(){
        if (agentApi.getAgentInfo() == null) {
            agentApi.setAgentInfo(new InternalAgentInfo());
        }
    }
    
}
