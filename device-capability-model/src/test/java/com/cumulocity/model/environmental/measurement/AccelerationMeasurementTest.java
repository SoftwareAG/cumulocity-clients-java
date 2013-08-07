package com.cumulocity.model.environmental.measurement;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class AccelerationMeasurementTest {

    private AccelerationMeasurement measurement;
    
    @Before
    public void setUp() throws Exception {
        
        AccelerationValue value = new AccelerationValue();
        value.setValue(new BigDecimal(10));
        value.setUnit("m/s2");

        measurement = new AccelerationMeasurement();
        measurement.setAcceleration(value);
    }

    @Test
    public final void shouldSerializeAndDeserializeCorrectly() {

        String serialized = JSON.defaultJSON().forValue(measurement);
        AccelerationMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(AccelerationMeasurement.class, serialized);
        assertEquals(measurement.getAcceleration(), newMeasurement.getAcceleration());
    }

}
