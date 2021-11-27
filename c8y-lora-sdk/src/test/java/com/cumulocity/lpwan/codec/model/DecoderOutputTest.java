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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoderOutputTest {

    @Test
    void doAddMeasurementsToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
        decoderOutput.addMeasurementToCreate(measurementRepresentation);

        assertEquals(measurementRepresentation, decoderOutput.getMeasurementsToCreate().get(0));
    }

    @Test
    void doAddMeasurementsToCreate_FailForNullMeasurement() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(null));
        assertEquals("DecoderOutput: 'measurement' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddAlarmsToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        decoderOutput.addAlarmToCreate(alarmRepresentation);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
    }

    @Test
    void doAddAlarmsToCreate_FailForNullAlarm() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(null));
        assertEquals("DecoderOutput: 'alarm' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddEventsToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        EventRepresentation eventRepresentation = new EventRepresentation();
        decoderOutput.addEventToCreate(eventRepresentation);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
    }

    @Test
    void doAddEventsToCreate_FailForNullEvent() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addEventToCreate(null));
        assertEquals("DecoderOutput: 'event' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddAlarmTypeToClear_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        decoderOutput.addAlarmTypeToClear("AAA");
        decoderOutput.addAlarmTypeToClear("BBB");

        assertTrue(decoderOutput.getAlarmTypesToClear().contains("AAA"));
        assertTrue(decoderOutput.getAlarmTypesToClear().contains("BBB"));
    }

    @Test
    void doAddAlarmTypeToClear_FailForNullOrEmpty() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmTypeToClear(null));
        assertEquals("DecoderOutput: 'alarmType' parameter can't be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmTypeToClear(""));
        assertEquals("DecoderOutput: 'alarmType' parameter can't be null or empty.", exception.getMessage());
    }

    @Test
    void doPropertyToUpdateDeviceMo_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        ManagedObjectProperty property = new ManagedObjectProperty("T", 10, "C");
        decoderOutput.addPropertyToUpdateDeviceMo(property);

        assertEquals(property, decoderOutput.getPropertiesToUpdateDeviceMo().get(0));
    }

    @Test
    void doPropertyToUpdateDeviceMo_FailForNull() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addPropertyToUpdateDeviceMo(null));
        assertEquals("DecoderOutput: 'managedObjectProperty' parameter can't be null.", exception.getMessage());
    }
}