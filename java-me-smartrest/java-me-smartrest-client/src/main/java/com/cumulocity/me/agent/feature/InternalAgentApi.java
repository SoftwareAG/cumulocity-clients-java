package com.cumulocity.me.agent.feature;

import com.cumulocity.me.agent.InternalAgentInfo;
import com.cumulocity.me.agent.bootstrap.ExternalIdProvider;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;
import com.cumulocity.me.agent.plugin.AgentApi;
import com.cumulocity.me.agent.push.DevicePushService;
import com.cumulocity.me.agent.smartrest.SmartrestService;

public class InternalAgentApi extends AgentApi{
    
    public InternalAgentInfo getInternalAgentInfo() {
        return agentInfo;
    }
    
    public void setAgentInfo(InternalAgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }
    
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setSmartrestService(SmartrestService smartrestService) {
        this.smartrestService = smartrestService;
    }

    public void setPushService(DevicePushService pushService) {
        this.pushService = pushService;
    }

    public void setDeviceInformationProvider(DeviceInformationProvider deviceInformationProvider) {
        this.deviceInformationProvider = deviceInformationProvider;
    }

    public void setExternalIdProvider(ExternalIdProvider externalIdProvider) {
        this.externalIdProvider = externalIdProvider;
    }
}
