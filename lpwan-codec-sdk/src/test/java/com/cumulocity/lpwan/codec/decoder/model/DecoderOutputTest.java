/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder.model;

import com.cumulocity.model.event.CumulocitySeverities;
import com.cumulocity.model.event.Severity;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoderOutputTest {

    @Test
    void doAddMeasurementToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
        decoderOutput.addMeasurementToCreate(measurementRepresentation);

        assertEquals(measurementRepresentation, decoderOutput.getMeasurementsToCreate().get(0));
    }

    @Test
    void doAddMeasurementToCreate_FailForNullMeasurement() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(null));
        assertEquals("DecoderOutput: 'measurement' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddAlarmToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        decoderOutput.addAlarmToCreate(alarmRepresentation);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
    }

    @Test
    void doAddAlarmToCreate_FailForNullAlarm() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(null));
        assertEquals("DecoderOutput: 'alarm' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddEventToCreate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        EventRepresentation eventRepresentation = new EventRepresentation();
        decoderOutput.addEventToCreate(eventRepresentation);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String event_type = "Event Type test";
        String event_text = "Event Text test";
        DateTime timeNow = DateTime.now();
        EventRepresentation eventRepresentation = decoderOutput.addEventToCreate(sourceId, event_type, event_text, timeNow);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getEventsToCreate().get(0).getSource().getId());
        assertEquals(event_type, decoderOutput.getEventsToCreate().get(0).getType());
        assertEquals(event_text, decoderOutput.getEventsToCreate().get(0).getText());
        assertEquals(timeNow, decoderOutput.getEventsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_EventTextAndTimeAsNull_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String event_type = "Event Type test";
        String event_text = null;
        DateTime timeNow = null;
        EventRepresentation eventRepresentation = decoderOutput.addEventToCreate(sourceId, event_type, event_text, timeNow);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getEventsToCreate().get(0).getSource().getId());
        assertEquals(event_type, decoderOutput.getEventsToCreate().get(0).getType());
        assertNull(decoderOutput.getEventsToCreate().get(0).getText());
        assertNotNull(decoderOutput.getEventsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_FailForNullSourceId() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = null;
        String event_type = "Event Type test";
        String event_text = "Event Text test";
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addEventToCreate(sourceId, event_type, event_text, timeNow));
        assertEquals("DecoderOutput: 'sourceId' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_FailForNullEventType() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String event_type = null;
        String event_text = "Event Text test";
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addEventToCreate(sourceId, event_type, event_text, timeNow));
        assertEquals("DecoderOutput: 'type' parameter can't be null or empty.", exception.getMessage());
    }

    @Test
    void doAddEventToCreate_FailForNullEvent() {
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
    void doAddAlarmToCreate_WithParametersMethod_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = "Alarm Type test";
        String alarm_text = "Alarm Text test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        DateTime timeNow = DateTime.now();
        AlarmRepresentation alarmRepresentation = decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_text, alarm_severity, timeNow);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getAlarmsToCreate().get(0).getSource().getId());
        assertEquals(alarm_type, decoderOutput.getAlarmsToCreate().get(0).getType());
        assertEquals(alarm_text, decoderOutput.getAlarmsToCreate().get(0).getText());
        assertEquals(alarm_severity.name(), decoderOutput.getAlarmsToCreate().get(0).getSeverity());
        assertEquals(timeNow, decoderOutput.getAlarmsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_AlarmTextSeverityAndTimeAsNull_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = "Alarm Type test";
        String alarm_text = null;
        Severity alarm_severity = null;
        DateTime timeNow = null;
        AlarmRepresentation alarmRepresentation = decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_text, alarm_severity, timeNow);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getAlarmsToCreate().get(0).getSource().getId());
        assertEquals(alarm_type, decoderOutput.getAlarmsToCreate().get(0).getType());
        assertNull(decoderOutput.getAlarmsToCreate().get(0).getText());
        assertNull(decoderOutput.getAlarmsToCreate().get(0).getSeverity());
        assertNotNull(decoderOutput.getAlarmsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_FailForNullSourceId() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = null;
        String alarm_type = "Alarm Type test";
        String alarm_text = "Alarm Text test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_text, alarm_severity, timeNow));
        assertEquals("DecoderOutput: 'sourceId' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_FailForNullAlarmType() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = null;
        String alarm_text = "Alarm Text test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_text, alarm_severity, timeNow));
        assertEquals("DecoderOutput: 'type' parameter can't be null or empty.", exception.getMessage());
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
    void doSetManagedObjectToUpdate_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        ManagedObjectRepresentation managedObjectRepresentation = ManagedObjects.asManagedObject(GId.asGId("111"));
        decoderOutput.setDeviceManagedObjectToUpdate(managedObjectRepresentation);

        assertEquals(managedObjectRepresentation, decoderOutput.getDeviceManagedObjectToUpdate());
    }

    @Test
    void doAddManagedObjectToUpdate_FailForNull() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.setDeviceManagedObjectToUpdate(null));
        assertEquals("DecoderOutput: 'deviceManagedObject' parameter can't be null.", exception.getMessage());
    }
}