package com.cumulocity.me.agent.plugin.impl;

import com.cumulocity.me.agent.binary.BinaryService;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.fieldbus.FieldbusService;
import com.cumulocity.me.agent.fieldbus.model.FieldbusDevice;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;
import com.cumulocity.me.agent.plugin.AgentApi;
import com.cumulocity.me.agent.provider.ExternalIdProvider;
import com.cumulocity.me.agent.push.DevicePushService;
import com.cumulocity.me.agent.restart.DeviceRestarter;
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

    public void setBinaryService(BinaryService binaryService){
        this.binaryService = binaryService;
    }

    public void setFieldbusService(FieldbusService fieldbusService) {
        this.fieldbusService = fieldbusService;
    }

    public void setDeviceRestarter(DeviceRestarter restarter) {
        this.restarter = restarter;
    }
}
