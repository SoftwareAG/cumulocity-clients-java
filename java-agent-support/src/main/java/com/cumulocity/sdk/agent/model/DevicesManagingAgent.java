/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.model;

import java.util.Collection;
import java.util.Queue;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

/**
 * Defines an agent managing some devices.
 * @param <D> type of devices managed by the agent.
 */
public interface DevicesManagingAgent<D extends Device> extends Agent {

	/**
	 * Gets all devices managed by the agent. 
	 * @return a collection of devices.
	 */
	Collection<D> getDevices();
	
	/**
	 * Gets a device of given ID.
	 * @param deviceId the device ID.
	 * @return the device of given ID.
	 */
	D getDevice(GId deviceId);
	
	/**
	 * Holds the queue of measurements retrieved from devices managed by the agent 
	 * to be uploaded to the <tt>Cumulocity</tt> platform.
	 * @return the queue of measurements.
	 */
	Queue<MeasurementRepresentation> getMeasurementsQueue();
	
	/**
	 * Holds the queue of operations retrieved from the <tt>Cumulocity</tt> platform
	 * to be executed on devices managed by the agent.
	 * @return the queue od commands.
	 */
	Queue<OperationRepresentation> getOperationsQueue();
}
