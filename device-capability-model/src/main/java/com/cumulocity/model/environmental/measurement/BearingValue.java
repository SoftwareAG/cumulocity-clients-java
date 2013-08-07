package com.cumulocity.model.environmental.measurement;

import java.math.BigDecimal;

import com.cumulocity.model.environmental.sensor.MotionSensor;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.model.measurement.StateType;
import com.cumulocity.model.measurement.ValueType;
import com.cumulocity.model.util.Alias;

/**
 * Represents a Bearing, as reported by, for example, {@link MotionSensor}.
 *
 */
@Alias(value = "c8y_BearingValue")
public class BearingValue extends MeasurementValue {

    public BearingValue() {
    }

    public BearingValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        super(value, unit, type, quantity, state);
    }
}
