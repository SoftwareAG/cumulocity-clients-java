package com.cumulocity.me.agent.feature;

public interface AgentFeature {
    public void init(InternalAgentApi agentApi) throws FeatureInitializationException;
    public void start() throws FeatureStartException;
    public void stop();
}
