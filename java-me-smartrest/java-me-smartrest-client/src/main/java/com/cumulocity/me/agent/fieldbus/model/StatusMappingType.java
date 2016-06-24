package com.cumulocity.me.agent.fieldbus.model;

import java.util.Vector;

public class StatusMappingType {
    private static final Vector VALUES = new Vector();

    public static final StatusMappingType READ = new StatusMappingType("read");
    public static final StatusMappingType WRITE = new StatusMappingType("write");

    public static StatusMappingType get(String string){
        for (int i = 0; i < VALUES.size(); i++) {
            StatusMappingType type = (StatusMappingType) VALUES.elementAt(i);
            if (type.getValue().equalsIgnoreCase(string)){
                return type;
            }
        }
        return null;
    }

    private final String value;

    private StatusMappingType(String value) {
        this.value = value;
        VALUES.addElement(this);
    }

    public String getValue() {
        return value;
    }
}
