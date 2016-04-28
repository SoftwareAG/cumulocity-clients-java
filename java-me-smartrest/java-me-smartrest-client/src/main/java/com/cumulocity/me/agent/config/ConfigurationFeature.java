package com.cumulocity.me.agent.config;

import com.cumulocity.me.agent.feature.AgentFeature;
import com.cumulocity.me.agent.feature.BaseFeature;
import com.cumulocity.me.agent.feature.FeatureInitializationException;
import com.cumulocity.me.agent.feature.InternalAgentApi;
import java.io.IOException;

public class ConfigurationFeature extends BaseFeature{
    private final ConfigurationReader reader;
    private final ConfigurationWriter writer;
    
    private ConfigurationService configurationService;

    public ConfigurationFeature(ConfigurationReader reader, ConfigurationWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }
    
    public void init(InternalAgentApi agentApi) throws FeatureInitializationException{
        try {
            configurationService = new ConfigurationService(reader, writer);
            agentApi.setConfigurationService(configurationService);
        } catch (IOException ex) {
            throw new FeatureInitializationException("Could not initialize config service: " + ex.getMessage());
        }
    }

    public void stop() {
        try {
            configurationService.write();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
}
