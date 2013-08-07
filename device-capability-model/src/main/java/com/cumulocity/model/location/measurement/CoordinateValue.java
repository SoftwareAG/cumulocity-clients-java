package com.cumulocity.model.location.measurement;

import java.math.BigDecimal;

import com.cumulocity.model.location.sensor.GPSSensor;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.model.measurement.StateType;
import com.cumulocity.model.measurement.ValueType;
import com.cumulocity.model.util.Alias;

/**
 * Represents a CoordinateValue, as reported by {@link GPSSensor}.
 *
 * At the moment, this representation does not provide any additional properties of its own.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_CoordinateValue")
public class CoordinateValue extends MeasurementValue {

    public CoordinateValue() {
    }

    public CoordinateValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        super(value, unit, type, quantity, state);
    }
}
