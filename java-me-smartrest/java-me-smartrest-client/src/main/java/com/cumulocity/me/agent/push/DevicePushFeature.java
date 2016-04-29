package com.cumulocity.me.agent.push;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.config.ConfigurationKey;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.feature.InternalAgentApi;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;

public class DevicePushFeature extends BaseFeature{

    private SmartHttpConnection connection;
    private DevicePushManager pushManager;
    
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        setupConnection();
        pushManager = new DevicePushManager(connection, agentApi.getInternalAgentInfo());
        agentApi.setPushService(pushManager);
    }
    
    public void start() {
        pushManager.start();
    }

    private void setupConnection(){
        System.out.println("DPF setup connection");
        ConfigurationService config = agentApi.getConfigurationService();
        System.out.println("got config");
        String credentials = agentApi.getInternalAgentInfo().getCredentials();
        System.out.println("got credentials");
        connection = new SmartHttpConnection(config.get(ConfigurationKey.CONNECTION_HOST_URL), AgentTemplates.XID, credentials);
        System.out.println("created credentials");
        connection.setupConnection(config.get(ConfigurationKey.CONNECTION_SETUP_PARAMS_STANDARD));
        System.out.println("setup connection");
    }
    
    public void stop() {
        pushManager.stop();
        connection.closeConnection();
    }
}
