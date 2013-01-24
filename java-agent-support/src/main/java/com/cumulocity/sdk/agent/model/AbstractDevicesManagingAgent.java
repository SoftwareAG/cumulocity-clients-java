/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.model;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.agent.util.PeekBlockingQueueWrapper;
import com.cumulocity.sdk.agent.util.UniqueElementsQueueWrapper;
import com.cumulocity.sdk.agent.util.OperationRepresentationByIdComparator;

/**
 * Base implementation of an agent that manages some devices.
 * @param <D> type of devices managed by the agent.
 */
public abstract class AbstractDevicesManagingAgent<D extends Device> extends AbstractAgent 
		implements DevicesManagingAgent<D> {

	private volatile Map<GId, D> devicesMap = 
			new ConcurrentHashMap<GId, D>();
	
	private Queue<MeasurementRepresentation> measurementsQueue = 
			new ConcurrentLinkedQueue<MeasurementRepresentation>();
	
	private Queue<OperationRepresentation> operationsQueue =
			new PeekBlockingQueueWrapper<OperationRepresentation>(
			new UniqueElementsQueueWrapper<OperationRepresentation>(
					new ConcurrentLinkedQueue<OperationRepresentation>(),
					OperationRepresentationByIdComparator.getInstance()));
	
	@Override
	public Collection<D> getDevices() {
		return devicesMap.values();
	}
	
	@Override
	public D getDevice(GId gid) {
		return devicesMap.get(gid);
	}

	public void setDevices(Collection<D> devices) {
		Map<GId, D> map = new ConcurrentHashMap<GId, D>(devices.size());
		for (D device : devices) {
			map.put(device.getGlobalId(), device);
		}
		devicesMap = map;
	}
	
	@Override
	public Queue<MeasurementRepresentation> getMeasurementsQueue() {
		return measurementsQueue;
	}
	
	@Override
	public Queue<OperationRepresentation> getOperationsQueue() {
		return operationsQueue;
	}
}
