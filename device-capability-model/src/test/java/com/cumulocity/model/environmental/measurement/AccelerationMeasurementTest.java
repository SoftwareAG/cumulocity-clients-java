package com.cumulocity.model.environmental.measurement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

import c8y.AccelerationMeasurement;

public class AccelerationMeasurementTest {

    private AccelerationMeasurement measurement;

    @BeforeEach
    void setUp() {
        measurement = new AccelerationMeasurement();
        measurement.setAccelerationValue(new BigDecimal(10));
    }

    @Test
    final void shouldSerializeAndDeserializeCorrectly() {
        String serialized = JSON.defaultJSON().forValue(measurement);
        AccelerationMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(AccelerationMeasurement.class, serialized);
        assertEquals(measurement.getAccelerationValue(), newMeasurement.getAccelerationValue());
    }
}
