package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class TemperatureMeasurement extends AbstractDynamicProperties {
    public static final String TEMP_UNIT = "C";

    private MeasurementValue t = new MeasurementValue(TEMP_UNIT);

    @JSONProperty("T")
    public MeasurementValue getT() {
        return t;
    }

    public void setT(MeasurementValue t) {
        this.t = t;
    }

    @JSONProperty(ignore = true)
    public BigDecimal getTemperature() {
        return t == null ? null : t.getValue();
    }

    public void setTemperature(BigDecimal temperature) {
        t = new MeasurementValue(TEMP_UNIT);
        t.setValue(temperature);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TemperatureMeasurement)) {
            return false;
        }

        TemperatureMeasurement rhs = (TemperatureMeasurement) obj;
        return t == null ? (rhs.t == null) : t.equals(rhs.t);
    }

    @Override
    public int hashCode() {
        return t == null ? 0 : t.hashCode();
    }
}
