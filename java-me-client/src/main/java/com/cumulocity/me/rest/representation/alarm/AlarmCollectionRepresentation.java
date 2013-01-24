package com.cumulocity.me.rest.representation.alarm;

import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class AlarmCollectionRepresentation extends BaseCollectionRepresentation {
    
    private List alarms;    

    public List getAlarms() {
        return alarms;
    }

    public void setAlarms(List alarms) {
        this.alarms = alarms;
    }
}
