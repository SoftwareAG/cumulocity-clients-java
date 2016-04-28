package com.cumulocity.me.agent.feature;

public abstract class BaseFeature implements AgentFeature{
    protected InternalAgentApi agentApi;

    public void init(InternalAgentApi agentApi) throws FeatureInitializationException{
        this.agentApi = agentApi;
    }

    public void start() throws FeatureStartException{ 
    }

    public void stop(){
    }
}
