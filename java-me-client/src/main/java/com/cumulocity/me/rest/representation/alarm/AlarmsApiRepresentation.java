package com.cumulocity.me.rest.representation.alarm;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class AlarmsApiRepresentation extends BaseCumulocityResourceRepresentation {

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

    public List getURITemplates() {
        List uriTemplates = new ArrayList();
        uriTemplates.add(getAlarmsForSource());
        uriTemplates.add(getAlarmsForSourceAndStatus());
        uriTemplates.add(getAlarmsForSourceAndStatusAndTime());
        uriTemplates.add(getAlarmsForSourceAndTime());
        uriTemplates.add(getAlarmsForStatus());
        uriTemplates.add(getAlarmsForStatusAndTime());
        uriTemplates.add(getAlarmsForTime());
        return uriTemplates;
    }
}
