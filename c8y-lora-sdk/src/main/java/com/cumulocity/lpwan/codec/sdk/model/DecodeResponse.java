package com.cumulocity.lpwan.codec.sdk.model;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DecodeResponse {
    private List<MeasurementRepresentation> measurementsToCreate;
    private List<EventRepresentation> eventsToCreate;
    private List<AlarmRepresentation> alarmsToCreate;
    private List<String> alarmTypesToClear;
    private List<ManagedObjectProperty> propertiesToAdd;
}
