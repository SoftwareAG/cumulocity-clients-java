package com.cumulocity.me.agent.fieldbus.model;

public class CoilDefinition {
    private final String name;
    private final int number;

    private final StatusMapping statusMapping;
    private final EventMapping eventMapping;
    private final AlarmMapping alarmMapping;


    public CoilDefinition(String name, int number, StatusMapping statusMapping, EventMapping eventMapping, AlarmMapping alarmMapping) {
        this.name = name;
        this.number = number;
        this.statusMapping = statusMapping;
        this.eventMapping = eventMapping;
        this.alarmMapping = alarmMapping;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
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

    public boolean hasStatusMapping(){
        return statusMapping != null;
    }

    public boolean hasAlarmMapping(){
        return alarmMapping != null;
    }

    public boolean hasEventMapping(){
        return eventMapping != null;
    }
}
