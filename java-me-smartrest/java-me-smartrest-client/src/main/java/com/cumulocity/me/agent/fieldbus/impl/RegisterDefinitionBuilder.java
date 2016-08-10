package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.*;

public class RegisterDefinitionBuilder {
    private String name;
    private int number;
    private int startBit;
    private int length;
    private String unit;
    private boolean signed;
    private boolean input;
    private Conversion conversion = Conversion.DEFAULT;
    private StatusMapping statusMapping;
    private EventMapping eventMapping;
    private AlarmMapping alarmMapping;
    private MeasurementMapping measurementMapping;

    public RegisterDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RegisterDefinitionBuilder withNumber(int number) {
        this.number = number;
        return this;
    }

    public RegisterDefinitionBuilder withStartBit(int startBit) {
        this.startBit = startBit;
        return this;
    }

    public RegisterDefinitionBuilder withLength(int length) {
        this.length = length;
        return this;
    }

    public RegisterDefinitionBuilder withSigned(boolean signed) {
        this.signed = signed;
        return this;
    }

    public RegisterDefinitionBuilder withInput(boolean input) {
        this.input = input;
        return this;
    }

    public RegisterDefinitionBuilder withConversion(Conversion conversion) {
        this.conversion = conversion;
        return this;
    }

    public RegisterDefinitionBuilder withStatusMapping(StatusMapping statusMapping) {
        this.statusMapping = statusMapping;
        return this;
    }

    public RegisterDefinitionBuilder withEventMapping(EventMapping eventMapping) {
        this.eventMapping = eventMapping;
        return this;
    }

    public RegisterDefinitionBuilder withAlarmMapping(AlarmMapping alarmMapping) {
        this.alarmMapping = alarmMapping;
        return this;
    }

    public RegisterDefinitionBuilder withMeasurementMapping(MeasurementMapping measurementMapping) {
        this.measurementMapping = measurementMapping;
        return this;
    }

    public RegisterDefinition build() {
        return new RegisterDefinition(name, number, startBit, length, signed, input, conversion, statusMapping, eventMapping, alarmMapping, measurementMapping);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getStartBit() {
        return startBit;
    }

    public int getLength() {
        return length;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isSigned() {
        return signed;
    }

    public boolean isInput() {
        return input;
    }

    public Conversion getConversion() {
        return conversion;
    }

    public StatusMapping getStatusMapping() {
        return statusMapping;
    }

    public EventMapping getEventMapping() {
        return eventMapping;
    }

    public AlarmMapping getAlarmMapping() {
        return alarmMapping;
    }

    public MeasurementMapping getMeasurementMapping() {
        return measurementMapping;
    }
}