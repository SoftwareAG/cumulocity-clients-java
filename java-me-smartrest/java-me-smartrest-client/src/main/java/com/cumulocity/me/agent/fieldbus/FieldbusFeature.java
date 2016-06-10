package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusBuffer;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

import java.util.Hashtable;

public class FieldbusFeature extends BaseFeature{

    private FieldbusService service;

    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        FieldbusBuffer buffer = new FieldbusBuffer();
        service = new FieldbusService(agentApi.getAgentInfo().getDeviceId(), buffer, agentApi.getSmartrestService());
        agentApi.setFieldbusService(service);
        agentApi.getSmartrestService().registerTemplates(FieldbusTemplates.INSTANCE);
    }
}
