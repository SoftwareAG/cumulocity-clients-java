/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.agent.model.AbstractAgent;
import com.cumulocity.sdk.client.Platform;

/**
 * Finds the agent representation in the platform.
 */
public class RepresentationLookupAction implements AgentAction {

    private static final Logger LOG = LoggerFactory.getLogger(RepresentationLookupAction.class);

    private Platform platform;
    
    private AbstractAgent agent;

    @Autowired
    public RepresentationLookupAction(Platform platform, AbstractAgent agent) {
		this.platform = platform;
		this.agent = agent;
	}

	@Override
    public void run() {
        if (agent.getGlobalId() == null) {
            String msg = "Agents globalId is unknown";
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
        try {
            ManagedObjectRepresentation managedObjectRepresentation = platform.getInventoryApi()
            		.getManagedObject(agent.getGlobalId()).get();
            agent.setAgentRepresentation(managedObjectRepresentation);
        } catch (Exception e) {
            String message = "Cannot fetch agent representation for " + agent.getGlobalId().toString();
            LOG.error(message);
            throw new RuntimeException(message, e);
        }
    }
}
