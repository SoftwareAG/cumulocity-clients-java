/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.mapping.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cumulocity.lpwan.payload.uplink.model.MeasurementMapping;

import lombok.Data;
import lombok.Singular;

@Data
public class MeasurementFragmentCollection {

    @Singular
    private Map<String, MeasurementFragment> measurementMappingsByType = new HashMap<String, MeasurementFragment>();
    
    public void put(String key, MeasurementFragment value) {
        measurementMappingsByType.put(key, value);
    }
    
    public MeasurementFragment get(String key) {
        return measurementMappingsByType.get(key);
    }
    
    public Set<Map.Entry<String, MeasurementFragment>> entrySet() {
        return measurementMappingsByType.entrySet();
    }
}
