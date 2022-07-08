package com.cumulocity.model.measurement;

import org.svenson.JSONProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MeasurementValue implements Serializable {

    private static final long serialVersionUID = 82786895631760488L;

    private BigDecimal value;

    private String unit;

    private ValueType type;

    private String quantity;

    private StateType state;

    public MeasurementValue() {
    }

    public MeasurementValue(String unit) {
        this.unit = unit;
    }

    public MeasurementValue(BigDecimal value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public MeasurementValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.quantity = quantity;
        this.state = state;
    }

    @JSONProperty(ignoreIfNull = false)
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @JSONProperty(ignoreIfNull = false)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JSONProperty(ignoreIfNull = true)
    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JSONProperty(ignoreIfNull = true)
    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("value", value);
        result.put("unit", unit);
        putIfNotNull(result, "quantity", quantity);
        putIfNotNull(result, "type", toStringOrNull(type));
        putIfNotNull(result, "state", toStringOrNull(state));
        return result;
    }

    private void putIfNotNull(HashMap<String, Object> result, String key, Object value) {
        if (value != null) {
            result.put(key, value);
        }
    }

    private String toStringOrNull(Object object) {
        return object == null ? null : object.toString();
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MeasurementValue))
            return false;

        MeasurementValue that = (MeasurementValue) o;

        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null)
            return false;
        if (state != that.state)
            return false;
        if (type != that.type)
            return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MeasurementValue [value=");
        builder.append(value);
        if (unit != null) {
            builder.append(", unit=");
            builder.append(unit);
        }
        if (type != null) {
            builder.append(", type=");
            builder.append(type);
        }
        if (quantity != null) {
            builder.append(", quantity=");
            builder.append(quantity);
        }
        if (state != null) {
            builder.append(", state=");
            builder.append(state);
        }
        builder.append("]");
        return builder.toString();
    }
}
