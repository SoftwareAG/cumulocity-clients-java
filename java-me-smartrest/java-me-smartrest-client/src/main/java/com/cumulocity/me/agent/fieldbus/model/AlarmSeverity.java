package com.cumulocity.me.agent.fieldbus.model;

import java.util.Vector;

public class AlarmSeverity {
    public static final AlarmSeverity WARNING = new AlarmSeverity("warning");
    public static final AlarmSeverity MINOR = new AlarmSeverity("minor");
    public static final AlarmSeverity MAJOR = new AlarmSeverity("major");
    public static final AlarmSeverity CRITICAL = new AlarmSeverity("critical");

    private static final Vector VALUES = new Vector();

    private static AlarmSeverity get(String value){
        for (int i = 0; i < VALUES.size(); i++) {
            AlarmSeverity alarmSeverity = (AlarmSeverity) VALUES.elementAt(i);
            if (alarmSeverity.getValue().equalsIgnoreCase(value)){
                return alarmSeverity;
            }
        }
        return null;
    }

    private final String value;

    private AlarmSeverity(String value) {
        this.value = value;
        VALUES.add(this);
    }

    public String getValue() {
        return value;
    }
}
