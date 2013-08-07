package com.cumulocity.model.environmental.measurement;

import java.math.BigDecimal;

import com.cumulocity.model.environmental.sensor.MotionSensor;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.model.measurement.StateType;
import com.cumulocity.model.measurement.ValueType;
import com.cumulocity.model.util.Alias;

/**
 * Represents a MotionValue, as reported by {@link MotionSensor}.
 *
 * At the moment, this representation does not provide any additional properties of its own.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_MotionValue")
public class MotionValue extends MeasurementValue {

    public MotionValue() {
    }

    public MotionValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        super(value, unit, type, quantity, state);
    }
}
