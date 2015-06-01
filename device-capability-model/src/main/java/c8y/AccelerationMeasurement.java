package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class AccelerationMeasurement extends AbstractDynamicProperties {
    public static final String ACC_UNIT = "m/s2";

    private MeasurementValue acceleration;

    public MeasurementValue getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(MeasurementValue acceleration) {
        this.acceleration = acceleration;
    }

    @JSONProperty(ignore = true)
    public BigDecimal getAccelerationValue() {
        return acceleration == null ? null : acceleration.getValue();
    }

    public void setAccelerationValue(BigDecimal acc) {
        acceleration = new MeasurementValue(ACC_UNIT);
        acceleration.setValue(acc);
    }

    @Override
    public int hashCode() {
        return acceleration != null ? acceleration.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AccelerationMeasurement)) {
            return false;
        }

        AccelerationMeasurement rhs = (AccelerationMeasurement) obj;
        return acceleration == null ? (rhs.acceleration == null)
                : acceleration.equals(rhs.acceleration);
    }

    @Override
    public String toString() {
        return "AccelerationMeasurement{" + "acceleration=" + acceleration
                + '}';
    }
}
