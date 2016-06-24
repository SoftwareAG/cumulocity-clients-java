package com.cumulocity.me.agent.smartrest;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.plugin.impl.InternalAgentInfo;
import com.cumulocity.me.agent.smartrest.impl.RequestBuffer;
import com.cumulocity.me.agent.smartrest.model.TemplateCollection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;

public class SmartrestService {

    private final RequestBuffer buffer;
    private final ConfigurationService configService;
    private final InternalAgentInfo agentInfo;

    public SmartrestService(RequestBuffer buffer, ConfigurationService configService, InternalAgentInfo agentInfo) {
        this.buffer = buffer;
        this.configService = configService;
        this.agentInfo = agentInfo;
    }

    public void sendRequest(SmartRequest request, String xId, SmartResponseEvaluator callback) {
        buffer.add(request, xId, callback);
    }

    public void sendRequest(SmartRequest request, String xId) {
        buffer.add(request, xId, null);
    }

    public void registerTemplates(TemplateCollection collection) {
        SmartHttpConnection connection = new SmartHttpConnection(configService.get(ConfigurationKey.CONNECTION_HOST_URL), collection.getXid(), agentInfo.getCredentials());
        connection.setupConnection(configService.get(ConfigurationKey.CONNECTION_SETUP_PARAMS_STANDARD));
        connection.templateRegistration(collection.buildTemplates());
        connection.closeConnection();
    }

    public void setOperationExecuting(String operationId) {
        SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_OPERATION_EXECUTING_REQUEST_MESSAGE_ID, operationId);
        sendRequest(request, AgentTemplates.XID);
    }
    
    public void setOperationSuccessful(String operationId) {
        SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_OPERATION_SUCCESSFUL_REQUEST_MESSAGE_ID, operationId);
        sendRequest(request, AgentTemplates.XID);
    }
    
    public void setOperationFailed(String operationId) {
        SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_OPERATION_FAILED_REQUEST_MESSAGE_ID, operationId);
        sendRequest(request, AgentTemplates.XID);
    }
    
    public void setOperationFailed(String operationId, String reason) {
    	if (reason == null) {
			setOperationFailed(operationId);
		} else {
			SmartRequest request = new SmartRequestImpl(AgentTemplates.UPDATE_OPERATION_FAILED_WITH_REASON_REQUEST_MESSAGE_ID, operationId + "," + reason);
			sendRequest(request, AgentTemplates.XID);
		}
    }
}
