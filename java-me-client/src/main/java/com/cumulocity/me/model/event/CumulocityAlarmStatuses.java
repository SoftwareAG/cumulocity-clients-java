package com.cumulocity.me.model.event;

public class CumulocityAlarmStatuses implements AlarmStatus {
    
    public static final CumulocityAlarmStatuses ACTIVE = new CumulocityAlarmStatuses("ACTIVE");
    
    public static final CumulocityAlarmStatuses ACKNOWLEDGED = new CumulocityAlarmStatuses("ACKNOWLEDGED");
    
    public static final CumulocityAlarmStatuses CLEARED = new CumulocityAlarmStatuses("CLEARED");
    
    private String name;
    
    private CumulocityAlarmStatuses(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
