package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public class FieldbusFeature extends BaseFeature{

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        ChildDeviceScanner childDeviceScanner = new ChildDeviceScanner(agentApi.getAgentInfo().getDeviceId(), agentApi.getSmartrestService());
        FieldbusService fieldbusService = new FieldbusService(childDeviceScanner);
        agentApi.setFieldbusService(fieldbusService);
    }
}
