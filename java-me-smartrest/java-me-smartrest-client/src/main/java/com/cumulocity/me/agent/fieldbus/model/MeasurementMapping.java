package com.cumulocity.me.agent.fieldbus.model;

public class MeasurementMapping {
    private final int template;
    private final String type;
    private final String series;

    public MeasurementMapping(int template, String type, String series) {
        this.template = template;
        this.type = type;
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public int getTemplate() {
        return template;
    }

    public String getSeries() {
        return series;
    }
}
