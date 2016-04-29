package com.cumulocity.me.agent.feature;

public interface AgentFeature {
    public void init(InternalAgentApi agentApi);
    public void start();
    public void stop();
}
