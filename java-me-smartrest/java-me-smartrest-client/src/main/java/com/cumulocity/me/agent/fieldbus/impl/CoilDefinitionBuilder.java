package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.AlarmMapping;
import com.cumulocity.me.agent.fieldbus.model.CoilDefinition;
import com.cumulocity.me.agent.fieldbus.model.EventMapping;
import com.cumulocity.me.agent.fieldbus.model.StatusMapping;

public class CoilDefinitionBuilder {

    private String name;
    private int number;
    private boolean input;
    private StatusMapping statusMapping;
    private EventMapping eventMapping;
    private AlarmMapping alarmMapping;

    public CoilDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CoilDefinitionBuilder withNumber(int number) {
        this.number = number;
        return this;
    }

    public CoilDefinitionBuilder withInput(boolean input) {
        this.input = input;
        return this;
    }

    public CoilDefinitionBuilder withStatusMapping(StatusMapping statusMapping) {
        this.statusMapping = statusMapping;
        return this;
    }

    public CoilDefinitionBuilder withEventMapping(EventMapping eventMapping) {
        this.eventMapping = eventMapping;
        return this;
    }

    public CoilDefinitionBuilder withAlarmMapping(AlarmMapping alarmMapping) {
        this.alarmMapping = alarmMapping;
        return this;
    }

    public CoilDefinition build() {
        return new CoilDefinition(name, number, input, statusMapping, eventMapping, alarmMapping);
    }
}
