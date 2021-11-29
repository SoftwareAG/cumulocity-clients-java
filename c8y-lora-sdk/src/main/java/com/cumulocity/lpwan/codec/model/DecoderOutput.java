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
import com.google.common.base.Strings;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * The DecoderOutput class represents the response format which may contain the Measurements/Events/Alarms to be created.
 * This may also contain the info about the alarm types to clear and the properties that to be added to the device managed object.
 */
@Data
public class DecoderOutput {
    private List<MeasurementRepresentation> measurementsToCreate;
    private List<EventRepresentation> eventsToCreate;
    private List<AlarmRepresentation> alarmsToCreate;
    private Set<String> alarmTypesToClear;
    private List<ManagedObjectProperty> propertiesToUpdateDeviceMo;

    static {
        //Registering the Joda module to serialize/deserialize the org.joda.time.DateTime
        new ObjectMapper().registerModule(new JodaModule());
    }

    public void addMeasurementToCreate(@NotNull final MeasurementRepresentation measurement) {
        if (Objects.isNull(measurementsToCreate)) {
            measurementsToCreate = new ArrayList<>();
        }
        if (Objects.isNull(measurement)) {
            throw new IllegalArgumentException("DecoderOutput: 'measurement' parameter can't be null.");
        }

        measurementsToCreate.add(measurement);
    }

    public void addEventToCreate(@NotNull EventRepresentation event) {
        if (Objects.isNull(eventsToCreate)) {
            eventsToCreate = new ArrayList<>();
        }
        if (Objects.isNull(event)) {
            throw new IllegalArgumentException("DecoderOutput: 'event' parameter can't be null.");
        }

        eventsToCreate.add(event);
    }

    public void addAlarmToCreate(@NotNull AlarmRepresentation alarm) {
        if (Objects.isNull(alarmsToCreate)) {
            alarmsToCreate = new ArrayList<>();
        }
        if (Objects.isNull(alarm)) {
            throw new IllegalArgumentException("DecoderOutput: 'alarm' parameter can't be null.");
        }

        alarmsToCreate.add(alarm);
    }

    public void addAlarmTypeToClear(@NotBlank String alarmType) {
        if (Objects.isNull(alarmTypesToClear)) {
            alarmTypesToClear = new HashSet<>();
        }
        if (Strings.isNullOrEmpty(alarmType)) {
            throw new IllegalArgumentException("DecoderOutput: 'alarmType' parameter can't be null or empty.");
        }

        alarmTypesToClear.add(alarmType);
    }

    public void addPropertyToUpdateDeviceMo(@NotNull ManagedObjectProperty managedObjectProperty) {
        if (Objects.isNull(propertiesToUpdateDeviceMo)) {
            propertiesToUpdateDeviceMo = new ArrayList<>();
        }
        if (Objects.isNull(managedObjectProperty)) {
            throw new IllegalArgumentException("DecoderOutput: 'managedObjectProperty' parameter can't be null.");
        }

        propertiesToUpdateDeviceMo.add(managedObjectProperty);
    }
}
