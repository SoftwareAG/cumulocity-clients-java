/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.model;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

/**
 * <p>
 * Defines an agent.
 * </p><p>
 * An agent is a function that fulfills three responsibilities for a given vendor and type of devices:
 * <ul>
 * <li>It translates the device-specific interface protocol into a single reference protocol.</li>
 * <li>It transform whatever domain model the device has to a reference domain model.</li>
 * <li>It enables secure remote communication in various network architectures.</li>
 * <ul>
 * </p>
 */
public interface Agent {

	/**
	 * @return the agent ID; unique within the agent type.
	 */
	String getExternalId();
	
	/**
	 * @return the type of agent and its ID.
	 */
	String getExternalIdType();
	
	/**
	 * @return the global ID of the agent on the <tt>Cumulocity</tt> platform.
	 */
	GId getGlobalId();
	
	/**
	 * @return the representation of an agent within the <tt>Cumolocity</tt> platform.
	 */
	ManagedObjectRepresentation getAgentRepresentation();
}
