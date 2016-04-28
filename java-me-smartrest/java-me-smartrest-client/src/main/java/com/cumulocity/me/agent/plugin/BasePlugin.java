package com.cumulocity.me.agent.plugin;

public abstract class BasePlugin implements AgentPlugin{

    protected AgentApi agentApi;
    
    public void init(AgentApi agentApi) {
        this.agentApi = agentApi;
    }

    public void enable() {
    }

    public void start() {
    }

    public void stop() {
    }
}
