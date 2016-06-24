package com.cumulocity.me.agent.fieldbus.model;

public class EventMapping {
    private final int template;
    private final String type;
    private final String text;

    public EventMapping(int template, String type, String text) {
        this.template = template;
        this.type = type;
        this.text = text;
    }

    public int getTemplate() {
        return template;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
}
