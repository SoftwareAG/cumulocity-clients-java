package com.cumulocity.me.agent.plugin;

public interface AgentPlugin {

    public void init(AgentApi agentApi);

    public void start(); 

    public void stop();
}
