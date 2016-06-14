package com.cumulocity.me.agent.feature;

import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;

public abstract class BaseFeature implements AgentFeature{
    protected InternalAgentApi agentApi;

    public void init(InternalAgentApi agentApi){
        this.agentApi = agentApi;
    }

    public void start(){ 
    }

    public void stop(){
    }
}
