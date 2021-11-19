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
import lombok.Data;

import java.util.List;

/**
 * The DecodeResponse class represents the response format which may contain the Measurements/Events/Alarms to be created.
 * This may also contain the info about the alarm types to clear and the properties that to be added to the device managed object.
 */
@Data
public class DecodeResponse {
    private List<MeasurementRepresentation> measurementsToCreate;
    private List<EventRepresentation> eventsToCreate;
    private List<AlarmRepresentation> alarmsToCreate;
    private List<String> alarmTypesToClear;
    private List<ManagedObjectProperty> propertiesToUpdateDeviceMo;
}
