/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.Platform;

/**
 * Uploads the device measurements to the platform.
 */
public class UploadDeviceMeasurementsAction implements AgentAction {

    private Platform platform;

    private DevicesManagingAgent<?> agent;

    @Autowired
    public UploadDeviceMeasurementsAction(Platform platform, DevicesManagingAgent<?> agent) {
        this.platform = platform;
        this.agent = agent;
    }

    @Override
    public void run() {
        MeasurementRepresentation measurement = agent.getMeasurementsQueue().poll();
        while (measurement != null) {
            uploadMeasurmentToPlatform(measurement);
            measurement = agent.getMeasurementsQueue().poll();
        }
    }

    protected void uploadMeasurmentToPlatform(MeasurementRepresentation measurement) {
        try {
            platform.getMeasurementApi().create(measurement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
