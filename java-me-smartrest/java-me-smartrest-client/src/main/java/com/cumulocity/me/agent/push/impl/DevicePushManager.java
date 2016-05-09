package com.cumulocity.me.agent.push.impl;

import java.util.Enumeration;
import java.util.Hashtable;

import com.cumulocity.me.agent.plugin.AgentInfo;
import com.cumulocity.me.agent.push.DevicePushService;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.agent.smartrest.model.TemplateCollection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartCometClient;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.util.SmartCometChannel;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class DevicePushManager implements DevicePushService{
	private static final Logger LOG = LoggerFactory.getLogger(DevicePushManager.class);
	
    private final SmartHttpConnection connection;
    private final SmartCometClient client;
    private final CombinedEvaluator combinedEvaluator;
    private final Hashtable supportedOperations;
    private final Hashtable xIds;
    private final AgentInfo agentInfo;
    private final SmartrestService smartrestService;
    
    public DevicePushManager(SmartHttpConnection connection, AgentInfo agentInfo, SmartrestService smartrestService) {
        this.connection = connection;
        this.combinedEvaluator = new CombinedEvaluator();
        this.client = new SmartCometClient(connection, combinedEvaluator);
        this.client.setReliable(true);
        this.supportedOperations = new Hashtable();
        this.agentInfo = agentInfo;
        this.xIds = new Hashtable();
        this.smartrestService = smartrestService;
    }
    
    public void registerOperation(String name, String xId, int messageId, SmartResponseEvaluator callback) {
        supportedOperations.put(name, name);
        xIds.put(xId, xId);
        combinedEvaluator.registerOperation(xId, messageId, callback);
    }
    
    public void start(){
        String channel = buildChannelString();
        client.startListenTo(SmartCometChannel.DEVICE_PUSH_ENDPOINT, new String[]{channel});
        LOG.info("Start device push");
        handleAlreadyExistingOperations();
    }
    
    private void handleAlreadyExistingOperations() {
		SmartRequest executingRequest = new SmartRequestImpl(TemplateCollection.GET_EXECUTING_OPERATIONS_MESSAGE_ID, agentInfo.getDeviceId());
		SmartRequest pendingRequest = new SmartRequestImpl(TemplateCollection.GET_PENDING_OPERATIONS_MESSAGE_ID, agentInfo.getDeviceId());
		LOG.info("Getting already existing operations");
    	Enumeration xIdEnumeration = xIds.keys();
        while (xIdEnumeration.hasMoreElements()) {
			String nextXId = (String) xIdEnumeration.nextElement();
			FixedXidCombinedEvaluator evaluator = new FixedXidCombinedEvaluator(combinedEvaluator, nextXId);
			smartrestService.sendRequest(executingRequest, nextXId, evaluator);
			smartrestService.sendRequest(pendingRequest, nextXId, evaluator);
		}
	}

	public void stop(){
        client.stopListenTo();
    }

    public String[] getSupportedOperations() {
        String[] supportedOperationsArray = new String[supportedOperations.size()];
        Enumeration supportedOperationsEnumeration = supportedOperations.keys();
        int index = 0;
        while (supportedOperationsEnumeration.hasMoreElements()) {
            String nextOperation = (String) supportedOperationsEnumeration.nextElement();
            supportedOperationsArray[index++] = nextOperation;
        }
        return supportedOperationsArray;
    }
    
    public String buildChannelString(){
        StringBuffer buffer = new StringBuffer("/");
        buffer.append(agentInfo.getDeviceId());
        Enumeration xIdEnumeration = xIds.keys();
        while (xIdEnumeration.hasMoreElements()) {
            String nextXId = (String) xIdEnumeration.nextElement();
            buffer.append(",").append(nextXId);
        }
        return buffer.toString();
    }
}
