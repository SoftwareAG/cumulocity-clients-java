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
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    void doAddMeasurementToCreate_WithParametersMethod_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String type = "c8y_Steam";
        String fragmentName = "c8y_Steam";
        String seriesName = "Temperature";
        BigDecimal value = BigDecimal.valueOf(10);
        String unit = "C";
        DateTime timeNow = DateTime.now();

        MeasurementRepresentation measurementRepresentation = decoderOutput.addMeasurementToCreate(sourceId, type, fragmentName, seriesName, value, unit, timeNow);
        assertEquals(sourceId, measurementRepresentation.getSource().getId());
        assertEquals(type, measurementRepresentation.getType());

        Object fragment = measurementRepresentation.getProperty(fragmentName);
        assertNotNull(fragment);
        assertTrue(fragment.getClass().isAssignableFrom(HashMap.class));
        Object series = ((Map)fragment).get(seriesName);
        assertTrue(series.getClass().isAssignableFrom(MeasurementValue.class));
        assertEquals(BigDecimal.valueOf(10), ((MeasurementValue)series).getValue());
        assertEquals("C", ((MeasurementValue)series).getUnit());
        assertEquals(timeNow, decoderOutput.getMeasurementsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddMeasurementToCreate_WithParametersMethod_FailForNullInputs() {
        DecoderOutput decoderOutput = new DecoderOutput();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(null, null, null, null, null, null, null));
        assertEquals("DecoderOutput: 'sourceId' parameter can't be null.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(GId.asGId("111"), null, null, null, null, null, null));
        assertEquals("DecoderOutput: 'type' parameter can't be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(GId.asGId("111"), "type", null, null, null, null, null));
        assertEquals("DecoderOutput: 'fragmentName' parameter can't be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addMeasurementToCreate(GId.asGId("111"), "type", "fragmentName", null, null, null, null));
        assertEquals("DecoderOutput: 'value' parameter can't be null.", exception.getMessage());
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
    void doAddAlarmToCreate_WithParametersMethod_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = "Alarm Type test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        String alarm_text = "Alarm Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        AlarmRepresentation alarmRepresentation = decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_severity, alarm_text, properties, timeNow);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getAlarmsToCreate().get(0).getSource().getId());
        assertEquals(alarm_type, decoderOutput.getAlarmsToCreate().get(0).getType());
        assertEquals(alarm_severity.name(), decoderOutput.getAlarmsToCreate().get(0).getSeverity());
        assertEquals(alarm_text, decoderOutput.getAlarmsToCreate().get(0).getText());
        assertEquals(properties, decoderOutput.getAlarmsToCreate().get(0).getAttrs());
        assertEquals(timeNow, decoderOutput.getAlarmsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_AlarmTextPropertiesAndTimeAsNull_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = "Alarm Type test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        AlarmRepresentation alarmRepresentation = decoderOutput.addAlarmToCreate(sourceId, alarm_type, alarm_severity, null, null, null);

        assertEquals(alarmRepresentation, decoderOutput.getAlarmsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getAlarmsToCreate().get(0).getSource().getId());
        assertEquals(alarm_type, decoderOutput.getAlarmsToCreate().get(0).getType());
        assertEquals(alarm_severity.name(), decoderOutput.getAlarmsToCreate().get(0).getSeverity());
        assertNull(decoderOutput.getAlarmsToCreate().get(0).getText());
        assertNotNull(decoderOutput.getAlarmsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_FailForNullSourceId() {
        DecoderOutput decoderOutput = new DecoderOutput();

        String alarm_type = "Alarm Type test";
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        String alarm_text = "Alarm Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(null, alarm_type, alarm_severity, alarm_text, properties, timeNow));
        assertEquals("DecoderOutput: 'sourceId' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_FailForNullAlarmType() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        Severity alarm_severity = CumulocitySeverities.CRITICAL;
        String alarm_text = "Alarm Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(sourceId, null, alarm_severity, alarm_text, properties, timeNow));
        assertEquals("DecoderOutput: 'type' parameter can't be null or empty.", exception.getMessage());
    }

    @Test
    void doAddAlarmToCreate_WithParametersMethod_FailForNullSeverity() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String alarm_type = "Alarm Type test";
        String alarm_text = "Alarm Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addAlarmToCreate(sourceId, alarm_type, null, alarm_text, properties, timeNow));
        assertEquals("DecoderOutput: 'severity' parameter can't be null or empty.", exception.getMessage());
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
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        EventRepresentation eventRepresentation = decoderOutput.addEventToCreate(sourceId, event_type, event_text, properties, timeNow);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getEventsToCreate().get(0).getSource().getId());
        assertEquals(event_type, decoderOutput.getEventsToCreate().get(0).getType());
        assertEquals(event_text, decoderOutput.getEventsToCreate().get(0).getText());
        assertEquals(properties, decoderOutput.getEventsToCreate().get(0).getAttrs());
        assertEquals(timeNow, decoderOutput.getEventsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_EventTextPropertiesAndTimeAsNull_Success() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String event_type = "Event Type test";
        EventRepresentation eventRepresentation = decoderOutput.addEventToCreate(sourceId, event_type, null, null, null);

        assertEquals(eventRepresentation, decoderOutput.getEventsToCreate().get(0));
        assertEquals(sourceId, decoderOutput.getEventsToCreate().get(0).getSource().getId());
        assertEquals(event_type, decoderOutput.getEventsToCreate().get(0).getType());
        assertNull(decoderOutput.getEventsToCreate().get(0).getText());
        assertTrue(decoderOutput.getEventsToCreate().get(0).getAttrs().isEmpty());
        assertNotNull(decoderOutput.getEventsToCreate().get(0).getDateTime());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_FailForNullSourceId() {
        DecoderOutput decoderOutput = new DecoderOutput();

        String event_type = "Event Type test";
        String event_text = "Event Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addEventToCreate(null, event_type, event_text, properties, timeNow));
        assertEquals("DecoderOutput: 'sourceId' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doAddEventToCreate_WithParametersMethod_FailForNullEventType() {
        DecoderOutput decoderOutput = new DecoderOutput();

        GId sourceId = GId.asGId("111");
        String event_text = "Event Text test";
        Map<String, Object> properties = new HashMap<>();
        properties.put("property-1", "proverty-1 value");
        DateTime timeNow = DateTime.now();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> decoderOutput.addEventToCreate(sourceId, null, event_text, properties, timeNow));
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