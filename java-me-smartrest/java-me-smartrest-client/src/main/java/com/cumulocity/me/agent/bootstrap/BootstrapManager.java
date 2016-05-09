package com.cumulocity.me.agent.bootstrap;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.bootstrap.exception.BootstrapFailedException;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.provider.ExternalIdProvider;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import java.io.IOException;

public class BootstrapManager {

	private static final Logger LOG = LoggerFactory.getLogger(BootstrapManager.class);
	
    private static final String BOOTSTRAP_CREDENTIALS = "Basic bWFuYWdlbWVudC9kZXZpY2Vib290c3RyYXA6RmhkdDFiYjFm";

    private final ConfigurationService configService;
    private final ExternalIdProvider deviceXIdProvider;
  
    private String credentials;

    private SmartHttpConnection connection;

    public BootstrapManager(ConfigurationService configService, ExternalIdProvider deviceXIdProvider) {
        this.configService = configService;
        this.deviceXIdProvider = deviceXIdProvider;  
    }

    private void loadCredentials() {
        LOG.info("loading credentials from file");
        credentials = configService.get(ConfigurationKey.AGENT_USER_CREDENTIALS);
    }
    
    private void saveCredentials(String credentials) throws BootstrapFailedException {
        configService.set(ConfigurationKey.AGENT_USER_CREDENTIALS, credentials);
        try {
            configService.write();
        } catch (IOException ex) {
            throw new BootstrapFailedException("Failed to save credentials");
        }
    }

    public SmartHttpConnection bootstrap() throws BootstrapFailedException {
        loadCredentials();
        LOG.info("Bootstrapping connection to " + configService.get(ConfigurationKey.CONNECTION_HOST_URL));
        if (credentials == null) {
        	LOG.debug("requesting credentials");
            connection = new SmartHttpConnection(configService.get(ConfigurationKey.CONNECTION_HOST_URL), AgentTemplates.XID,  BOOTSTRAP_CREDENTIALS);
            connection.setupConnection(configService.get(ConfigurationKey.CONNECTION_SETUP_PARAMS_STANDARD));
            credentials = connection.bootstrap(deviceXIdProvider.getExternalId());
            LOG.debug("received credentials");
            saveCredentials(credentials);
        } else {
        	LOG.debug("credentials already found, setting up connection");
            connection = new SmartHttpConnection(configService.get(ConfigurationKey.CONNECTION_HOST_URL), AgentTemplates.XID, credentials);
            connection.setupConnection(configService.get(ConfigurationKey.CONNECTION_SETUP_PARAMS_STANDARD));
            LOG.debug("connection setup done");
        }
        connection.templateRegistration(AgentTemplates.TEMPLATES);
        return connection;
    }

    public String getCredentials() {
        return credentials;
    }
}
