package com.cumulocity.microservice.customdecoders.api.model;

import com.cumulocity.microservice.customdecoders.api.util.ObjectUtils;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.JSONTypeHint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Setter
public class DecoderResult implements Serializable {

    private List<AlarmRepresentation> internalServiceAlarms;

    private List<EventRepresentation> internalServiceEvents;

    private List<AlarmRepresentation> alarms;

    private List<EventRepresentation> events;

    private List<MeasurementDto> measurements;

    private List<DataFragmentUpdate> dataFragments;

    @Getter
    private String message;

    @Getter
    private boolean success = true;

    public static final DecoderResult empty() {
        return new DecoderResult();
    }

    public final DecoderResult setAsFailed(String message) {
        success = false;
        this.message = message;
        clearParsedData();
        // Clean incomplete parsed data and returning internal alarms and events
        alarms = internalServiceAlarms;
        events = internalServiceEvents;
        return this;
    }

    public void addAlarm(AlarmRepresentation alarmRepresentation, boolean internal) {
        if(ObjectUtils.isNull(alarms)) {
            alarms = new ArrayList<>();
        }
        alarms.add(alarmRepresentation);
        if(internal) {
            addInternalAlarm(alarmRepresentation);
        }
    }

    public void addAlarms(Collection<AlarmRepresentation> alarmRepresentations) {
        if(ObjectUtils.isNull(alarms)) {
            alarms = new ArrayList<>();
        }
        alarms.addAll(alarmRepresentations);
    }

    public void addEvent(EventRepresentation eventRepresentation, boolean internal) {
        if(ObjectUtils.isNull(events)) {
            events = new ArrayList<>();
        }
        events.add(eventRepresentation);
        if(internal) {
            addInternalEvent(eventRepresentation);
        }
    }

    public void addMeasurement(MeasurementDto measurementDto) {
        if(ObjectUtils.isNull(measurements)) {
            measurements = new ArrayList<>();
        }
        measurements.add(measurementDto);
    }

    public void addMeasurements(Collection<MeasurementDto> measurementDtoCollection) {
        if(ObjectUtils.isNull(measurements)) {
            measurements = new ArrayList<>();
        }
        measurements.addAll(measurementDtoCollection);
    }

    public void addDataFragment(DataFragmentUpdate dataFragmentUpdate) {
        if(ObjectUtils.isNull(dataFragments)) {
            dataFragments = new ArrayList<>();
        }
        dataFragments.add(dataFragmentUpdate);
    }

    private void addInternalAlarm(AlarmRepresentation alarmRepresentation) {
        if(ObjectUtils.isNull(internalServiceAlarms)) {
            internalServiceAlarms = new ArrayList<>();
        }
        internalServiceAlarms.add(alarmRepresentation);
    }

    private void addInternalEvent(EventRepresentation eventRepresentation) {
        if(ObjectUtils.isNull(internalServiceEvents)) {
            internalServiceEvents = new ArrayList<>();
        }
        internalServiceEvents.add(eventRepresentation);
    }

    private void clearParsedData() {
        if(!ObjectUtils.isNull(alarms)) {
            alarms.clear();
        }
        if(!ObjectUtils.isNull(events)) {
            events.clear();
        }
        if(!ObjectUtils.isNull(measurements)) {
            measurements.clear();
        }
        if(!ObjectUtils.isNull(dataFragments)) {
            dataFragments.clear();
        }
    }

    @JSONTypeHint(AlarmRepresentation.class)
    public List<AlarmRepresentation> getAlarms() {
        return alarms;
    }

    @JSONTypeHint(EventRepresentation.class)
    public List<EventRepresentation> getEvents() {
        return events;
    }

    @JSONTypeHint(MeasurementDto.class)
    public List<MeasurementDto> getMeasurements() {
        return measurements;
    }

    @JSONTypeHint(DataFragmentUpdate.class)
    public List<DataFragmentUpdate> getDataFragments() {
        return dataFragments;
    }
}
