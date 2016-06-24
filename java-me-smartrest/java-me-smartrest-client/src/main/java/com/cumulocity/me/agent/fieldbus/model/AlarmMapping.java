package com.cumulocity.me.agent.fieldbus.model;

public class AlarmMapping {
    private final int template;
    private final String type;
    private final String text;
    private final AlarmSeverity severity;

    public AlarmMapping(int template, String type, String text, AlarmSeverity severity) {
        this.template = template;
        this.type = type;
        this.text = text;
        this.severity = severity;
    }

    public int getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public AlarmSeverity getSeverity() {
        return severity;
    }
}
