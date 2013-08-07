package com.cumulocity.model.environmental.measurement;

import com.cumulocity.model.environmental.sensor.AccelerationSensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for an acceleration measurement, as reported by {@link AccelerationSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Acceleration</a> for details.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_AccelerationMeasurement")
public class AccelerationMeasurement  {

    private AccelerationValue acceleration;

    public AccelerationMeasurement() {
    }

    public AccelerationMeasurement(AccelerationValue acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * @return the acceleration
     */
    public final AccelerationValue getAcceleration() {
        return acceleration;
    }

    /**
     * @param acceleration the acceleration to set
     */
    public final void setAcceleration(AccelerationValue acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public int hashCode() {
        return acceleration != null ? acceleration.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccelerationMeasurement)) return false;

        AccelerationMeasurement that = (AccelerationMeasurement) o;

        if (acceleration != null ? !acceleration.equals(that.acceleration) : that.acceleration != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "AccelerationMeasurement{" +
                "acceleration=" + acceleration +
                '}';
    }
}
