package com.cumulocity.rest.representation.alarm;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class AlarmCollectionRepresentation extends BaseCollectionRepresentation<AlarmRepresentation> {

    private List<AlarmRepresentation> alarms;

    @JSONTypeHint(AlarmRepresentation.class)
    public List<AlarmRepresentation> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<AlarmRepresentation> alarms) {
        this.alarms = alarms;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<AlarmRepresentation> iterator() {
        return alarms.iterator();
    }
}
