package com.cumulocity.model;

import static com.cumulocity.model.util.ExtensibilityConverter.classToStringRepresentation;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

public class ID extends AbstractDynamicProperties {

    private String type;

    private String value;

    private String name;

    public ID() {
        this.type = classToStringRepresentation(getClass());
        this.value = null;
    }

    public ID(String id) {
        this.type = classToStringRepresentation(getClass());
        this.value = id;
    }

    public ID(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public ID(String type, String value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    public ID(ID in) {
        this.setType(in.getType());
        this.setValue(in.getValue());
        this.setName(in.getName());
        for (String name : propertyNames()) {
            this.setProperty(name, in.getProperty(name));
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JSONProperty(ignore = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ID)) return false;

        ID id = (ID) o;

        if (type != null ? !type.equals(id.type) : id.type != null) return false;
        if (value != null ? !value.equals(id.value) : id.value != null) return false;

        return true;
    }

    /**
     * This method compares if two ids have the same content (type, value) even if they have different classes. This method is useful for the identity service.
     * @param o the ID to compare with this instance
     * @return true if both ID have the same contents (type, value) regardless of the class, false otherwise
     */
    public boolean equalsIgnoreClass(Object o) {
        if (this == o) return true;
        if (!(o instanceof ID)) return false;

        ID id = (ID) o;

        if (type != null ? !type.equalsIgnoreCase(id.type) : id.type != null) return false;
        if (value != null ? !value.equalsIgnoreCase(id.value) : id.value != null) return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID [type=").append(type).append(", value=").append(value).append("]");
        return builder.toString();
    }

    public static String asString(ID id) {
        return id == null ? null : id.getValue();
    }
}
