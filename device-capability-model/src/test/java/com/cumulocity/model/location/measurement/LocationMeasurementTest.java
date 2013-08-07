package com.cumulocity.model.location.measurement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class LocationMeasurementTest {

    private LocationMeasurement measurement;
    
    @Before
    public void setUp() throws Exception {
        CoordinateValue value1 = new CoordinateValue();
        value1.setValue(new BigDecimal(10));
        CoordinateValue value2 = new CoordinateValue();
        value2.setValue(new BigDecimal(20));
        CoordinateValue value3 = new CoordinateValue();
        value3.setValue(new BigDecimal(30));

        measurement = new LocationMeasurement();
        measurement.setLatitude(value1);
        measurement.setLongitude(value2);
        measurement.setAltitude(value3);
    }

    @Test
    public final void shouldSerializeAndDeserializeCorrectly() {

        String serialized = JSON.defaultJSON().forValue(measurement);
        LocationMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(LocationMeasurement.class, serialized);
        assertTrue(serialized.contains("\"latitude\":"));    // According to wiki page
        assertTrue(serialized.contains("\"longitude\":"));    // According to wiki page
        assertTrue(serialized.contains("\"altitude\":"));    // According to wiki page
        assertEquals(measurement.getLatitude(), newMeasurement.getLatitude());
        assertEquals(measurement.getLongitude(), newMeasurement.getLongitude());
        assertEquals(measurement.getAltitude(), newMeasurement.getAltitude());
        
    }

}
