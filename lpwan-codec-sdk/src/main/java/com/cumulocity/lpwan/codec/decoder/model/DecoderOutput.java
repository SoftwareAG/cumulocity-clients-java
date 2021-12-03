/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder.model;

import com.cumulocity.model.event.Severity;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.measurement.MeasurementValue;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * The <b>DecoderOutput</b> class represents the response format which may contain the Measurements/Events/Alarms to be created.
 * This may also contain the info about the alarm types to clear and the properties that to be added to the device managed object.
 *
 *  @author Bhaskar Reddy Byreddy
 *  @author Atul Kumar Panda
 *  @version 1.0
 *  @since   2021-12-01
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

    /**
     * This method adds a measurement to the measurement-list to be created.
     *
     * @param measurement  A non-null MeasurementRepresentation object that is to be added to the list.
     * @throws IllegalArgumentException if the MeasurementRepresentation object (that is to be created) is null.
     * @see IllegalArgumentException
     */
    public void addMeasurementToCreate(@NotNull final MeasurementRepresentation measurement) {
        if (Objects.isNull(measurement)) {
            throw new IllegalArgumentException("DecoderOutput: 'measurement' parameter can't be null.");
        }

        if (Objects.isNull(measurementsToCreate)) {
            measurementsToCreate = new ArrayList<>();
        }

        measurementsToCreate.add(measurement);
    }

    /**
     * This method takes in the measurement parameters to create a MeasurementRepresentation object.
     *
     * @param sourceId     A non-null that parameter represents the GId(ManagedObject ID) of the device on which the measurement has to be created.
     * @param type         A non-null and non-empty parameter that represents the type of the measurement.
     * @param fragmentName A non-null and non-empty parameter that represents the name of the fragment in the measurement.
     * @param seriesName   represents the series name for the measurement fragment.
     * @param value        A non-null parameter that represents the value for the measurement fragment
     * @param unit         represents the unit of the value in the measurement fragment.
     * @param time         represents the time at which the measurement is created.
     * @return MeasurementRepresentation
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
     */
    public MeasurementRepresentation addMeasurementToCreate(@NotNull GId sourceId, @NotBlank String type, @NotBlank String fragmentName, @Nullable String seriesName, @NotNull BigDecimal value, @Nullable String unit, @Nullable DateTime time) {
        if (Objects.isNull(sourceId)) {
            throw new IllegalArgumentException("DecoderOutput: 'sourceId' parameter can't be null.");
        }
        if (Strings.isNullOrEmpty(type)) {
            throw new IllegalArgumentException("DecoderOutput: 'type' parameter can't be null or empty.");
        }
        if (Strings.isNullOrEmpty(fragmentName)) {
            throw new IllegalArgumentException("DecoderOutput: 'fragmentName' parameter can't be null or empty.");
        }
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("DecoderOutput: 'value' parameter can't be null.");
        }

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(sourceId);

        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
        measurementRepresentation.setSource(source);
        measurementRepresentation.setType(type);

        MeasurementValue measurementValue = new MeasurementValue(value, unit);
        if (Strings.isNullOrEmpty(seriesName)) {
            measurementRepresentation.setProperty(fragmentName, measurementValue);
        }
        else {
            Map<String, MeasurementValue> seriesMap = new HashMap<>();
            seriesMap.put(seriesName, measurementValue);

            measurementRepresentation.setProperty(fragmentName, seriesMap);
        }

        if (Objects.nonNull(time)) {
            measurementRepresentation.setDateTime(time);
        }
        else {
            measurementRepresentation.setDateTime(DateTime.now());
        }

        addMeasurementToCreate(measurementRepresentation);

        return measurementRepresentation;
    }

    /**
     * This method adds an event to the event-list to be created.
     *
     * @param event  A non-null EventRepresentation object that is to be added to the list.
     * @throws IllegalArgumentException if the EventRepresentation object (that is to be created) is null.
     * @see IllegalArgumentException
     */
    public void addEventToCreate(@NotNull EventRepresentation event) {
        if (Objects.isNull(event)) {
            throw new IllegalArgumentException("DecoderOutput: 'event' parameter can't be null.");
        }

        if (Objects.isNull(eventsToCreate)) {
            eventsToCreate = new ArrayList<>();
        }

        eventsToCreate.add(event);
    }

    /**
     * This method takes in the event parameters to create an EventRepresentation object.
     *
     * @param sourceId   A non-null parameter that represents the GId(ManagedObject ID) of the device on which the event has to be created.
     * @param type       A non-null and non-empty parameter that represents the type of the event.
     * @param text       represents the event text
     * @param properties represents the event properties
     * @param time       represents the time at which the event is created.
     * @return EventRepresentation
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
     */
    public EventRepresentation addEventToCreate(@NotNull GId sourceId, @NotBlank String type, @Nullable String text, @Nullable Map<String, Object> properties, @Nullable DateTime time) {
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

        if (Objects.nonNull(properties)) {
            event.setAttrs(properties);
        }

        if (Objects.nonNull(time)) {
            event.setDateTime(time);
        }
        else {
            event.setDateTime(DateTime.now());
        }

        addEventToCreate(event);

        return event;
    }

    /**
     * This method adds an alarm to the alarm-list to be created.
     *
     * @param alarm  A non-null AlarmRepresentation object that is to be added to the list.
     * @throws IllegalArgumentException if the AlarmRepresentation object (that is to be created) is null.
     * @see IllegalArgumentException
     */
    public void addAlarmToCreate(@NotNull AlarmRepresentation alarm) {
        if (Objects.isNull(alarm)) {
            throw new IllegalArgumentException("DecoderOutput: 'alarm' parameter can't be null.");
        }

        if (Objects.isNull(alarmsToCreate)) {
            alarmsToCreate = new ArrayList<>();
        }

        alarmsToCreate.add(alarm);
    }

    /**
     * This method takes in the alarm parameters to create an AlarmRepresentation object.
     *
     * @param sourceId   A non-null parameter that represents the GId(ManagedObject ID) of the device on which the alarm has to be created.
     * @param type       A non-null and non-empty parameter that represents the type of the alarm.
     * @param severity   A non-null parameter that represents the severity of the alarm.
     * @param text       represents the alarm text
     * @param properties represents the alarm properties
     * @param time       represents the time at which the alarm is created.
     * @return AlarmRepresentation
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see IllegalArgumentException
     */
    public AlarmRepresentation addAlarmToCreate(@NotNull GId sourceId, @NotBlank String type, @NotNull Severity severity, @Nullable String text, @Nullable Map<String, Object> properties, @Nullable DateTime time) {
        if (Objects.isNull(sourceId)) {
            throw new IllegalArgumentException("DecoderOutput: 'sourceId' parameter can't be null.");
        }
        if (Strings.isNullOrEmpty(type)) {
            throw new IllegalArgumentException("DecoderOutput: 'type' parameter can't be null or empty.");
        }
        if (Objects.isNull(severity)) {
            throw new IllegalArgumentException("DecoderOutput: 'severity' parameter can't be null or empty.");
        }

        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(ManagedObjects.asManagedObject(sourceId));
        alarm.setType(type);
        alarm.setText(text);
        alarm.setSeverity(severity.name());

        if (Objects.nonNull(properties)) {
            alarm.setAttrs(properties);
        }

        if (Objects.nonNull(time)) {
            alarm.setDateTime(time);
        }
        else {
            alarm.setDateTime(DateTime.now());
        }

        addAlarmToCreate(alarm);

        return alarm;
    }


    /**
     * This method adds an alarm type to be cleared.
     *
     * @param alarmType  A non-null and non-empty alarm type that is to be cleared.
     * @throws IllegalArgumentException if the alarmType (that is to be cleared) is null or empty.
     * @see IllegalArgumentException
     */
    public void addAlarmTypeToClear(@NotBlank String alarmType) {
        if (Strings.isNullOrEmpty(alarmType)) {
            throw new IllegalArgumentException("DecoderOutput: 'alarmType' parameter can't be null or empty.");
        }

        if (Objects.isNull(alarmTypesToClear)) {
            alarmTypesToClear = new HashSet<>();
        }

        alarmTypesToClear.add(alarmType);
    }

    /**
     * This method updates the managed object of the device.
     *
     * @param deviceManagedObject  A non-null device managed object that is to be updated.
     * @throws IllegalArgumentException if the alarmType (that is to be cleared) is null or empty.
     * @see IllegalArgumentException
     */
    public void setDeviceManagedObjectToUpdate(@NotNull ManagedObjectRepresentation deviceManagedObject) {
        if (Objects.isNull(deviceManagedObject)) {
            throw new IllegalArgumentException("DecoderOutput: 'deviceManagedObject' parameter can't be null.");
        }

        deviceManagedObjectToUpdate = deviceManagedObject;
    }
}
