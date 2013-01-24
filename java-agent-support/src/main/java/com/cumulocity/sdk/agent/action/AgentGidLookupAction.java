/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.idtype.XtId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.sdk.agent.model.AbstractAgent;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.SDKException;

/**
 * Finds the agent global ID in the platform.
 */
public class AgentGidLookupAction implements AgentAction {

    private static final Logger LOG = LoggerFactory.getLogger(AgentGidLookupAction.class);

    private static final int DEFAULT_LOOKUP_INTERVAL = 3000;

    private Platform platform;

    private AbstractAgent agent;

    private int retryInterval = DEFAULT_LOOKUP_INTERVAL;

    @Autowired
    public AgentGidLookupAction(Platform platform, AbstractAgent agent) {
        this.platform = platform;
        this.agent = agent;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    @Override
    public void run() {
        if (agent.getExternalId() == null) {
            LOG.error("No externalId specified. Please configure agent properly");
            throw new RuntimeException("No externalId specified. Please configure agent properly");
        }
        GId agentGId = lookupGidWaitUntilAvailable();
        agent.setGlobalId(agentGId);
    }

    private GId lookupGidWaitUntilAvailable() {
        GId gid = null;
        while (gid == null) {
            try {
                XtId extId = new XtId(agent.getExternalId());
                extId.setType(agent.getExternalIdType());
                ExternalIDRepresentation idRepresentation = platform.getIdentityApi().getExternalId(extId);
                gid = idRepresentation.getManagedObject().getId();
            } catch (SDKException e) {
                if (e.getHttpStatus() == 404) {
                    LOG.info("Binding for " + externalIdString() + " is not available. Waiting...");

                    try {
                        Thread.sleep(retryInterval);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw new RuntimeException(e);
                }

            }
        }
        return gid;
    }

    private String externalIdString() {
        return format("externalId:(type: %s, value: %s)", agent.getExternalIdType(), agent.getExternalId());
    }
}
