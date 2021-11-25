/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The DecoderOutput class represents the response format which may contain the Measurements/Events/Alarms to be created.
 * This may also contain the info about the alarm types to clear and the properties that to be added to the device managed object.
 */
@Data
public class DecoderOutput {
    private List<MeasurementRepresentation> measurementsToCreate;
    private List<EventRepresentation> eventsToCreate;
    private List<AlarmRepresentation> alarmsToCreate;
    private List<String> alarmTypesToClear;
    private List<ManagedObjectProperty> propertiesToUpdateDeviceMo;

    static {
        //Registering the Joda module to serialize/deserialize the org.joda.time.DateTime
        new ObjectMapper().registerModule(new JodaModule());
    }

    public void addMeasurementToCreate(MeasurementRepresentation measurement) {
        if (Objects.isNull(measurementsToCreate)) {
            measurementsToCreate = new ArrayList<>();
        }
        measurementsToCreate.add(measurement);
    }

    public void addEventToCreate(EventRepresentation event) {
        if (Objects.isNull(eventsToCreate)) {
            eventsToCreate = new ArrayList<>();
        }
        eventsToCreate.add(event);
    }

    public void addAlarmToCreate(AlarmRepresentation alarm) {
        if (Objects.isNull(alarmsToCreate)) {
            alarmsToCreate = new ArrayList<>();
        }
        alarmsToCreate.add(alarm);
    }

    public void addAlarmTypeToClear(String alarmType) {
        if (Objects.isNull(alarmTypesToClear)) {
            alarmTypesToClear = new ArrayList<>();
        }
        alarmTypesToClear.add(alarmType);
    }

    public void addPropertyToUpdateDeviceMo(ManagedObjectProperty managedObjectProperty) {
        if (Objects.isNull(propertiesToUpdateDeviceMo)) {
            propertiesToUpdateDeviceMo = new ArrayList<>();
        }
        propertiesToUpdateDeviceMo.add(managedObjectProperty);
    }
}
