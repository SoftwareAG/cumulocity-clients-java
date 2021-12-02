/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder.model;

import com.cumulocity.model.event.Severity;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Strings;
import lombok.Data;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * The DecoderOutput class represents the response format which may contain the Measurements/Events/Alarms to be created.
 * This may also contain the info about the alarm types to clear and the properties that to be added to the device managed object.
 */
@Data
public class DecoderOutput {
    @Nullable
    private List<MeasurementRepresentation> measurementsToCreate;

    @Nullable
    private List<EventRepresentation> eventsToCreate;

    @Nullable
    private List<AlarmRepresentation> alarmsToCreate;

    @Nullable
    private Set<String> alarmTypesToClear;

    @Nullable
    private ManagedObjectRepresentation deviceManagedObjectToUpdate;

    static {
        // Registering the Joda module to serialize/deserialize the org.joda.time.DateTime
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

    public EventRepresentation addEventToCreate(@NotNull GId sourceId, @NotBlank String type, @Nullable String text, @Nullable DateTime time) {
        if (Objects.isNull(sourceId)) {
            throw new IllegalArgumentException("DecoderOutput: 'sourceId' parameter can't be null.");
        }
        if (Strings.isNullOrEmpty(type)) {
            throw new IllegalArgumentException("DecoderOutput: 'type' parameter can't be null or empty.");
        }

        EventRepresentation event = new EventRepresentation();
        event.setSource(ManagedObjects.asManagedObject(sourceId));
        event.setType(type);
        event.setText(text);
        if(Objects.nonNull(time)) {
            event.setDateTime(time);
        }
        addEventToCreate(event);

        return event;
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

    public AlarmRepresentation addAlarmToCreate(@NotNull GId sourceId, @NotBlank String type, @Nullable String text, @Nullable Severity severity, @Nullable DateTime time) {
        if (Objects.isNull(sourceId)) {
            throw new IllegalArgumentException("DecoderOutput: 'sourceId' parameter can't be null.");
        }
        if (Strings.isNullOrEmpty(type)) {
            throw new IllegalArgumentException("DecoderOutput: 'type' parameter can't be null or empty.");
        }

        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(ManagedObjects.asManagedObject(sourceId));
        alarm.setType(type);
        alarm.setText(text);
        if(Objects.nonNull(severity)) {
            alarm.setSeverity(severity.name());
        }
        if(Objects.nonNull(time)) {
            alarm.setDateTime(time);
        }
        addAlarmToCreate(alarm);

        return alarm;
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

    public void setDeviceManagedObjectToUpdate(@NotNull ManagedObjectRepresentation deviceManagedObject) {
        if (Objects.isNull(deviceManagedObject)) {
            throw new IllegalArgumentException("DecoderOutput: 'deviceManagedObject' parameter can't be null.");
        }

        deviceManagedObjectToUpdate = deviceManagedObject;
    }
}
