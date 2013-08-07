package com.cumulocity.model.environmental.measurement;

import java.math.BigDecimal;

import com.cumulocity.model.environmental.sensor.TemperatureSensor;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.model.measurement.StateType;
import com.cumulocity.model.measurement.ValueType;
import com.cumulocity.model.util.Alias;

/**
 * Represents a TemperatureValue, as reported by {@link TemperatureSensor}.
 *
 * At the moment, this representation does not provide any additional properties of its own.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_TemperatureValue")
public class TemperatureValue extends MeasurementValue {

    private static final long serialVersionUID = 1901601265538630754L;

    public TemperatureValue() {
    }

    public TemperatureValue(BigDecimal value, String unit, ValueType type, String quantity, StateType state) {
        super(value, unit, type, quantity, state);
    }
}
