package com.cumulocity.me.agent.fieldbus.model;

public class RegisterDefinition {
    private final String name;
    private final int number;
    private final int startBit;
    private final int length;
    private final boolean signed;
    private final boolean input;

    private final Conversion conversion;
    private final StatusMapping statusMapping;
    private final EventMapping eventMapping;
    private final AlarmMapping alarmMapping;
    private final MeasurementMapping measurementMapping;

    public RegisterDefinition(String name, int number, int startBit, int length, boolean signed, boolean input, Conversion conversion, StatusMapping statusMapping, EventMapping eventMapping, AlarmMapping alarmMapping, MeasurementMapping measurementMapping) {
        this.name = name;
        this.number = number;
        this.startBit = startBit;
        this.length = length;
        this.signed = signed;
        this.input = input;
        this.conversion = conversion;
        this.statusMapping = statusMapping;
        this.eventMapping = eventMapping;
        this.alarmMapping = alarmMapping;
        this.measurementMapping = measurementMapping;
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

    public boolean isSigned() {
        return signed;
    }

    public boolean isInput() {
        return input;
    }

    public StatusMapping getStatusMapping() {
        return statusMapping;
    }

    public Conversion getConversion() {
        return conversion;
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

    public boolean hasStatusMapping(){
        return statusMapping != null;
    }

    public boolean hasAlarmMapping(){
        return alarmMapping != null;
    }

    public boolean hasEventMapping(){
        return eventMapping != null;
    }

    public boolean hasMeasurementMapping(){
        return measurementMapping != null;
    }
}
