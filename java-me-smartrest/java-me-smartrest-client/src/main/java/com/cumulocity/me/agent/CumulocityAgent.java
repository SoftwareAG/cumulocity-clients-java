package com.cumulocity.me.agent;

import com.cumulocity.me.agent.feature.AgentFeature;
import com.cumulocity.me.agent.plugin.AgentPlugin;
import com.cumulocity.me.agent.plugin.impl.InternalAgentApi;
import com.cumulocity.me.sdk.SDKException;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class CumulocityAgent {

    private static final Logger LOG = LoggerFactory.getLogger(CumulocityAgent.class);

    private final AgentPlugin[] plugins;
    private final AgentFeature[] features;
    private final InternalAgentApi agentApi;

    public CumulocityAgent(AgentFeature[] features, AgentPlugin[] plugins) {
        this.plugins = plugins;
        this.features = features;
        this.agentApi = new InternalAgentApi();
    }

    public void launch() {
        try {
            initFeatures();
            initPlugins();
            startFeatures();
            startPlugins();
        } catch (SDKException e) {
            LOG.warn("Starting agent failed", e);
            sleepQuietly(10000);
            launch();
        }
    }

    private void sleepQuietly(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            //ignore
        }
    }

    public void stop() {
        stopPlugins();
        stopFeatures();
    }

    private void initFeatures() {
        for (int i = 0; i < features.length; i++) {
            AgentFeature feature = features[i];
            feature.init(agentApi);
        }
    }

    private void initPlugins() {
        for (int i = 0; i < plugins.length; i++) {
            AgentPlugin plugin = plugins[i];
            plugin.init(agentApi);
        }
    }

    private void startFeatures() {
        for (int i = 0; i < features.length; i++) {
            AgentFeature feature = features[i];
            feature.start();
        }
    }

    private void startPlugins() {
        for (int i = 0; i < plugins.length; i++) {
            AgentPlugin plugin = plugins[i];
            plugin.start();
        }
    }

    private void stopPlugins() {
        for (int i = 0; i < plugins.length; i++) {
            AgentPlugin plugin = plugins[i];
            plugin.stop();

        }
    }

    private void stopFeatures() {
        for (int i = features.length - 1; i >= 0; i--) {

            AgentFeature feature = features[i];
            feature.stop();

        }
    }
}
