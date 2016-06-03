package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusBuffer;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public class FieldbusFeature extends BaseFeature{

    private FieldbusService service;

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        ChildDeviceScanner childDeviceScanner = new ChildDeviceScanner(agentApi.getAgentInfo().getDeviceId(), agentApi.getSmartrestService());
        FieldbusBuffer buffer = new FieldbusBuffer();
        service = new FieldbusService(childDeviceScanner, buffer);
        agentApi.setFieldbusService(service);
    }

    public void start() {
        service.scanChildDevices();
    }
}
