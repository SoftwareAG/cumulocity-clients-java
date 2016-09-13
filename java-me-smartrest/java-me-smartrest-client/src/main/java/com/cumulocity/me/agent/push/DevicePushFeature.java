package com.cumulocity.me.agent.push;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;
import com.cumulocity.me.agent.push.impl.DevicePushManager;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;

import javax.microedition.io.Connector;

public class DevicePushFeature extends BaseFeature{

    private SmartHttpConnection connection;
    private DevicePushManager pushManager;
    
    public void init(InternalAgentApi agentApi) {
        super.init(agentApi);
        setupConnection();
        pushManager = new DevicePushManager(connection, agentApi.getInternalAgentInfo(), agentApi.getSmartrestService());
        agentApi.setPushService(pushManager);
    }
    
    public void start() {
        pushManager.start();
    }

    private void setupConnection(){
        ConfigurationService config = agentApi.getConfigurationService();
        String credentials = agentApi.getInternalAgentInfo().getCredentials();
        connection = new SmartHttpConnection(config.get(ConfigurationKey.CONNECTION_HOST_URL), AgentTemplates.XID, credentials);
        connection.setHeartbeatCheckInterval(getHeartbeatCheckInterval());
        connection.setupConnection(Connector.READ_WRITE, true);
        connection.setupConnection(config.get(ConfigurationKey.CONNECTION_SETUP_PARAMS_REALTIME));
    }
    
    public void stop() {
        pushManager.stop();
        connection.closeConnection();
    }

    public int getHeartbeatCheckInterval() {
        ConfigurationService config = agentApi.getConfigurationService();
        Integer value = config.getInt(ConfigurationKey.CONNECTION_HEARTBEAT_CHECK_INTERVAL);
        if (value == null) {
            return -1;
        }
        return value.intValue() * 1000;
    }
}
