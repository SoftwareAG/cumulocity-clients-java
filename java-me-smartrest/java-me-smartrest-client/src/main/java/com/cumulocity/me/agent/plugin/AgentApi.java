package com.cumulocity.me.agent.plugin;

import com.cumulocity.me.agent.binary.BinaryService;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.fieldbus.FieldbusService;
import com.cumulocity.me.agent.fieldbus.model.FieldbusDevice;
import com.cumulocity.me.agent.integration.DeviceInformationProvider;
import com.cumulocity.me.agent.plugin.impl.InternalAgentInfo;
import com.cumulocity.me.agent.provider.ExternalIdProvider;
import com.cumulocity.me.agent.push.DevicePushService;
import com.cumulocity.me.agent.smartrest.SmartrestService;

public abstract class AgentApi {
    protected ExternalIdProvider externalIdProvider;
    protected DeviceInformationProvider deviceInformationProvider;
    protected InternalAgentInfo agentInfo;
    protected ConfigurationService configurationService;
    protected SmartrestService smartrestService;
    protected DevicePushService pushService;
    protected FieldbusService fieldbusService;
    protected BinaryService binaryService;

    public AgentInfo getAgentInfo(){
        return agentInfo;
    }
    
    public ConfigurationService getConfigurationService(){
        return configurationService;
    }

    public ExternalIdProvider getExternalIdProvider() {
        return externalIdProvider;
    }

    public DeviceInformationProvider getDeviceInformationProvider() {
        return deviceInformationProvider;
    }

    public SmartrestService getSmartrestService() {
        return smartrestService;
    }

    public DevicePushService getPushService() {
        return pushService;
    }

    public BinaryService getBinaryService() {
        return binaryService;
    }

    public FieldbusService getFieldbusService() {
        return fieldbusService;
    }
}
