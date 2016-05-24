package com.cumulocity.me.agent.fieldbus.model;

import java.util.Hashtable;

public class StatusMapping {
    private final StatusMappingType type;
    private final IntegerStringMap enumValues;

    public StatusMapping(StatusMappingType type, IntegerStringMap enumValues) {
        this.type = type;
        this.enumValues = enumValues;
    }

    public StatusMappingType getType() {
        return type;
    }

    public String getEnumValue(Integer key){
        if (enumValues == null) {
            return null;
        }
        return enumValues.get(key);
    }

    public String getEnumValue(int key){
        if (enumValues == null) {
            return null;
        }
        return enumValues.get(new Integer(key));
    }
}
