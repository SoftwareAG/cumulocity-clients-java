/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ManagedObjectPropertyTest {

    @Test
    void doCreate_WithNameValueAndUnit_Success() {
        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Temperature", -17.7778, "C");
        assertEquals("c8y_Temperature", property.getName());
        assertEquals(-17.7778, property.getValue());
        assertEquals("C", property.getUnit());

        property = new ManagedObjectProperty("c8y_Temperature", "100", null);
        assertEquals("c8y_Temperature", property.getName());
        assertEquals("100", property.getValue());
        assertNull(property.getUnit());
    }

    @Test
    void doCreate_WithNameNull_Fail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ManagedObjectProperty(null, null, null));
        assertEquals("ManagedObjectProperty: 'name' parameter can't be null or empty.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> new ManagedObjectProperty(null, null));
        assertEquals("ManagedObjectProperty: 'name' parameter can't be null or empty.", exception.getMessage());
    }

    @Test
    void doCreate_WithNameAndChildProperties_Success() {
        List<ManagedObjectProperty> childProperties = new ArrayList<>();
        childProperties.add(new ManagedObjectProperty("TC", -17.7778));
        childProperties.add(new ManagedObjectProperty("TF", 32, "F"));

        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Temperature", childProperties);
        assertEquals("c8y_Temperature", property.getName());
        assertEquals(childProperties, property.getChildProperties());
    }

    @Test
    void doAddChildProperty_FailForNull() {
        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Temperature", null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> property.addChildProperty(null));
        assertEquals("ManagedObjectProperty: 'childProperty' parameter can't be null.", exception.getMessage());
    }

    @Test
    void doGetPropertyAsMap_FragmentWithOnlyValue() {
        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Temperature", -17.7778);

        assertTrue(property.getPropertyAsMap().containsKey("c8y_Temperature"));
        assertEquals(-17.7778, property.getPropertyAsMap().get("c8y_Temperature"));
    }

    @Test
    void doGetPropertyAsMap_FragmentWithValueAndUnit() {
        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Temperature", 32, "F");

        Map<String, Object> propertyAsMap = property.getPropertyAsMap();
        assertTrue(propertyAsMap.containsKey("c8y_Temperature"));

        Map<String, Object> series = (Map<String, Object>) propertyAsMap.get("c8y_Temperature");
        assertTrue(series.containsKey("value"));
        assertEquals(32, series.get("value"));

        assertTrue(series.containsKey("unit"));
        assertEquals("F", series.get("unit"));
    }

    @Test
    void doGetPropertyAsMap_FragmentWithMultipleSeries() {
        ManagedObjectProperty property = new ManagedObjectProperty("c8y_Position", null);

        property.addChildProperty(new ManagedObjectProperty("alt", 67, "metre"));
        property.addChildProperty(new ManagedObjectProperty("lng", 6.15173));
        property.addChildProperty(new ManagedObjectProperty("lat", 51.211977));

        Map<String, Object> propertyAsMap = property.getPropertyAsMap();
        assertTrue(propertyAsMap.containsKey("c8y_Position"));

        Map<String, Object> series = (Map<String, Object>) propertyAsMap.get("c8y_Position");

        assertTrue(series.containsKey("alt"));
        Map<String, Object> alt_series = (Map<String, Object>) series.get("alt");
        assertTrue(alt_series.containsKey("value"));
        assertEquals(67, alt_series.get("value"));

        assertTrue(alt_series.containsKey("unit"));
        assertEquals("metre", alt_series.get("unit"));

        assertTrue(series.containsKey("lng"));
        assertEquals(6.15173, series.get("lng"));

        assertTrue(series.containsKey("lat"));
        assertEquals(51.211977, series.get("lat"));
    }
}