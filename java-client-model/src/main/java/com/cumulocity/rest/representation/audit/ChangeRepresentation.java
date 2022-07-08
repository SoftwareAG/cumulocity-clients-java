package com.cumulocity.rest.representation.audit;

import com.cumulocity.model.audit.AuditChangeValueConverter;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ChangeRepresentation extends AbstractExtensibleRepresentation {

    public enum ChangeType {
        REPLACED, ADDED, REMOVED
    }

    private String attribute;

    private String type;

    private Object previousValue;

    private Object newValue;

    @Setter
    private ChangeType changeType;

    @JSONProperty(ignoreIfNull = true)
    public ChangeType getChangeType() {
        return changeType;
    }

    @Builder
    public ChangeRepresentation(String attribute, String type, Object previousValue, Object newValue) {
        this.attribute = attribute;
        this.type = type;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public String getType() {
        return type;
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

    @JSONConverter(type = AuditChangeValueConverter.class)
    public Object getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Object previous) {
        this.previousValue = previous;
    }

    @JSONConverter(type = AuditChangeValueConverter.class)
    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object current) {
        this.newValue = current;
    }

    @Override
    public String toString() {
        return "ChangeRepresentation [attribute=" + attribute + ", type=" + type + ", previousValue=" + previousValue + ", newValue=" + newValue + "]";
    }
}
