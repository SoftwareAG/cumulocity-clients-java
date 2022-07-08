package com.cumulocity.rest.representation.alarm;

import java.util.ArrayList;
import java.util.List;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.svenson.JSONProperty;

public class AlarmsApiRepresentation extends AbstractExtensibleRepresentation {

    private AlarmCollectionRepresentation alarms;

    private String alarmsForStatus;

    private String alarmsForSource;

    private String alarmsForSourceAndStatus;

    private String alarmsForTime;

    private String alarmsForStatusAndTime;

    private String alarmsForSourceAndTime;

    private String alarmsForSourceAndStatusAndTime;

    public AlarmCollectionRepresentation getAlarms() {
        return alarms;
    }

    public void setAlarms(AlarmCollectionRepresentation alarms) {
        this.alarms = alarms;
    }

    public String getAlarmsForStatus() {
        return alarmsForStatus;
    }

    public void setAlarmsForStatus(String alarmsForStatus) {
        this.alarmsForStatus = alarmsForStatus;
    }

    public String getAlarmsForSource() {
        return alarmsForSource;
    }

    public void setAlarmsForSource(String alarmsForSource) {
        this.alarmsForSource = alarmsForSource;
    }

    public String getAlarmsForSourceAndStatus() {
        return alarmsForSourceAndStatus;
    }

    public void setAlarmsForSourceAndStatus(String alarmsForSourceAndStatus) {
        this.alarmsForSourceAndStatus = alarmsForSourceAndStatus;
    }

    public String getAlarmsForTime() {
        return alarmsForTime;
    }

    public void setAlarmsForTime(String alarmsForTime) {
        this.alarmsForTime = alarmsForTime;
    }

    public String getAlarmsForStatusAndTime() {
        return alarmsForStatusAndTime;
    }

    public void setAlarmsForStatusAndTime(String alarmsForStatusAndTime) {
        this.alarmsForStatusAndTime = alarmsForStatusAndTime;
    }

    public String getAlarmsForSourceAndTime() {
        return alarmsForSourceAndTime;
    }

    public void setAlarmsForSourceAndTime(String alarmsForSourceAndTime) {
        this.alarmsForSourceAndTime = alarmsForSourceAndTime;
    }

    public String getAlarmsForSourceAndStatusAndTime() {
        return alarmsForSourceAndStatusAndTime;
    }

    public void setAlarmsForSourceAndStatusAndTime(String alarmsForSourceAndStatusAndTime) {
        this.alarmsForSourceAndStatusAndTime = alarmsForSourceAndStatusAndTime;
    }

    @JSONProperty(ignore = true)
    public List<String> getURITemplates() {
        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add(this.getAlarmsForSource());
        uriTemplates.add(this.getAlarmsForSourceAndStatus());
        uriTemplates.add(this.getAlarmsForSourceAndStatusAndTime());
        uriTemplates.add(this.getAlarmsForSourceAndTime());
        uriTemplates.add(this.getAlarmsForStatus());
        uriTemplates.add(this.getAlarmsForStatusAndTime());
        uriTemplates.add(this.getAlarmsForTime());
        return uriTemplates;
    }

}
