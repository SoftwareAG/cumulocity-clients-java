package com.cumulocity.me.model.audit;

public class Change {

    private String attribute;

    private String type;

    private Object previousValue;

    private Object newValue;

    public String getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type.getName();
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Object previous) {
        this.previousValue = previous;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object current) {
        this.newValue = current;
    }

}
