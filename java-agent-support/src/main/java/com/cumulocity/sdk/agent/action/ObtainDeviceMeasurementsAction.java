/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.driver.DeviceDriver;
import com.cumulocity.sdk.agent.driver.DeviceException;
import com.cumulocity.sdk.agent.model.Device;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;

/**
 * Base implementation of action retrieving device measurements.
 * @param <D> type of devices managed by the agent.
 */
public class ObtainDeviceMeasurementsAction<D extends Device> implements AgentAction {

    private static final Logger LOG = LoggerFactory.getLogger(ObtainDeviceMeasurementsAction.class);
    
	private DevicesManagingAgent<D> agent;
    
    private DeviceDriver<D> deviceDriver;
	
    /**
     * @param agent the agent controlling some devices to get measurements from.
     */
    @Autowired
	public ObtainDeviceMeasurementsAction(DevicesManagingAgent<D> agent) {
		this.agent = agent;
	}
    
    public void setDeviceDriver(DeviceDriver<D> deviceDriver) {
		this.deviceDriver = deviceDriver;
	}

	@Override
	public void run() {
		Collection<D> devices = new ArrayList<D>(agent.getDevices());
		
		for (D device : devices) {
		    try {
    			List<MeasurementRepresentation> measurements = deviceDriver.loadMeasuremntsFromDevice(device);
    			agent.getMeasurementsQueue().addAll(measurements);
		    } catch (DeviceException e) {
		        LOG.error("Device error when obtaining measurements!", e);
		    }
		}
	}
	
}
