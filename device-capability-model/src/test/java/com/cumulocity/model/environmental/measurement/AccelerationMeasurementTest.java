package com.cumulocity.model.environmental.measurement;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

import c8y.AccelerationMeasurement;

public class AccelerationMeasurementTest {

    private AccelerationMeasurement measurement;
    
    @Before
    public void setUp() throws Exception {
    	measurement = new AccelerationMeasurement();
        measurement.setAccelerationValue(new BigDecimal(10));
    }

    @Test
    public final void shouldSerializeAndDeserializeCorrectly() {
        String serialized = JSON.defaultJSON().forValue(measurement);
        AccelerationMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(AccelerationMeasurement.class, serialized);
        assertEquals(measurement.getAccelerationValue(), newMeasurement.getAccelerationValue());
    }

}
