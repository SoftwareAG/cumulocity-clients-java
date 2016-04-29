package com.cumulocity.me.agent.integration;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.plugin.AgentApi;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;

public class DeviceUpdateHandler {
    private final AgentApi agentApi;
    
    public DeviceUpdateHandler(AgentApi agentApi) {
        this.agentApi = agentApi;
    }

    public void updateDevice(){
        updateHardware();
        updateSupportedOperations();
    }
    
    private void updateHardware(){
        String hardwareData = buildHardwareString();
        SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_HARDWARE_REQUEST_MESSAGE_ID, hardwareData);
        agentApi.getSmartrestService().sendRequest(request, AgentTemplates.XID);
    }
    
    private String buildHardwareString(){
        StringBuffer buffer = new StringBuffer(agentApi.getAgentInfo().getDeviceId()).append(",");
        DeviceInformationProvider infoProvider = agentApi.getDeviceInformationProvider();
        buffer.append(infoProvider.getModel()).append(",");
        buffer.append(infoProvider.getRevision()).append(",");
        buffer.append(infoProvider.getSerialNumber());
        return buffer.toString();
    }
    
    private void updateSupportedOperations(){
        String supprtedOperationsData = buildOperationsString();
        SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_SUPPOERTED_OPERATIONS_REQUEST_MESSAGE_ID, supprtedOperationsData);
        agentApi.getSmartrestService().sendRequest(request, AgentTemplates.XID);
    }
    
    private String buildOperationsString(){
        StringBuffer buffer = new StringBuffer(agentApi.getAgentInfo().getDeviceId()).append(",");
        buffer.append("\"");
        String[] operations = agentApi.getPushService().getSupportedOperations();
        for (int i = 0; i < operations.length; i++) {
            String operation = operations[i];
            if (i != 0) {
                buffer.append(",");
            }
            buffer.append("\"\"").append(operation).append("\"\"");
        }
        buffer.append("\"");
        return buffer.toString();
    }
}
