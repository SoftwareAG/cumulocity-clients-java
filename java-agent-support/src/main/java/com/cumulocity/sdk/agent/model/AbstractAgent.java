/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.model;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * Base class for agent implementations.
 */
public abstract class AbstractAgent implements Agent {
	
	private GId globalId;
	
	private ManagedObjectRepresentation agentRepresentation;
	
	@Override
	public GId getGlobalId() {
		return globalId;
	}
	
	public void setGlobalId(GId globalId) {
		this.globalId = globalId;
	}
	
	@Override
	public ManagedObjectRepresentation getAgentRepresentation() {
		return agentRepresentation;
	}

	public void setAgentRepresentation(ManagedObjectRepresentation agentRepresentation) {
		this.agentRepresentation = agentRepresentation;
	}
}
