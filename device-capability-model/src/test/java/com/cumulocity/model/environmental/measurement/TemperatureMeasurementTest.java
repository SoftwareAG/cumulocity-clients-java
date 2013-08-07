package com.cumulocity.model.environmental.measurement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class TemperatureMeasurementTest {

    private TemperatureMeasurement measurement;

    @Before
    public void setUp() throws Exception {

        TemperatureValue value = new TemperatureValue();
        value.setValue(new BigDecimal(32.0));
        value.setUnit("C");

        measurement = new TemperatureMeasurement();
        measurement.setTemperature(value);
    }

    @Test
    public final void shouldSerializeAndDeserializeCorrectly() {

        String serialized = JSON.defaultJSON().forValue(measurement);
        TemperatureMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(TemperatureMeasurement.class, serialized);
        assertTrue(serialized.contains("\"T\":")); // According to wiki page
        assertEquals(measurement.getTemperature().getValue(), newMeasurement.getTemperature().getValue());
    }
}
