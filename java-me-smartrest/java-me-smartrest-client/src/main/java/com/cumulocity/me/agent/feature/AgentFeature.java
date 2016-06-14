package com.cumulocity.me.agent.feature;

import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public interface AgentFeature {
    public void init(InternalAgentApi agentApi);
    public void start();
    public void stop();
}
