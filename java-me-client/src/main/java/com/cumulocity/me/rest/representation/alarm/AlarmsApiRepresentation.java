/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.rest.representation.alarm;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;

public class AlarmsApiRepresentation extends BaseResourceRepresentation {

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
