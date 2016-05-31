package com.cumulocity.me.agent.fieldbus.model;

import java.util.Hashtable;

public class StatusMapping {
    private final StatusMappingType type;

    public StatusMapping(StatusMappingType type) {
        this.type = type;
    }

    public StatusMappingType getType() {
        return type;
    }
}
