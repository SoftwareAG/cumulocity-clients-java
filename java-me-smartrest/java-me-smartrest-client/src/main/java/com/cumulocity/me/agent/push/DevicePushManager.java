package com.cumulocity.me.agent.push;

import com.cumulocity.me.agent.CumulocityAgent;
import com.cumulocity.me.agent.plugin.AgentInfo;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartCometClient;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.util.SmartCometChannel;
import java.util.Enumeration;
import java.util.Hashtable;

public class DevicePushManager implements DevicePushService{

    private final SmartHttpConnection connection;
    private final SmartCometClient client;
    private final CombinedEvaluator combinedEvaluator;
    private final Hashtable supportedOperations;
    private final Hashtable xIds;
    private final AgentInfo agentInfo;
    
    public DevicePushManager(SmartHttpConnection connection, AgentInfo agentInfo) {
        this.connection = connection;
        this.combinedEvaluator = new CombinedEvaluator();
        this.client = new SmartCometClient(connection, combinedEvaluator);
        this.client.setReliable(true);
        this.supportedOperations = new Hashtable();
        this.agentInfo = agentInfo;
        this.xIds = new Hashtable();
    }
    
    public void registerOperation(String name, String xId, int messageId, SmartResponseEvaluator callback) {
        supportedOperations.put(name, name);
        xIds.put(xId, xId);
        combinedEvaluator.registerOperation(xId, messageId, callback);
    }
    
    public void start(){
        String channel = buildChannelString();
        client.startListenTo(SmartCometChannel.DEVICE_PUSH_ENDPOINT, new String[]{channel});
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
