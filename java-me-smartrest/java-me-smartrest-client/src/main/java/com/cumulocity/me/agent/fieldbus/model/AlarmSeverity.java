package com.cumulocity.me.agent.fieldbus.model;

import java.util.Vector;

public class AlarmSeverity {
    private static final Vector VALUES = new Vector();

    public static final AlarmSeverity WARNING = new AlarmSeverity("WARNING");
    public static final AlarmSeverity MINOR = new AlarmSeverity("MINOR");
    public static final AlarmSeverity MAJOR = new AlarmSeverity("MAJOR");
    public static final AlarmSeverity CRITICAL = new AlarmSeverity("CRITICAL");

    public static AlarmSeverity get(String value){
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
        VALUES.addElement(this);
    }

    public String getValue() {
        return value;
    }
}
