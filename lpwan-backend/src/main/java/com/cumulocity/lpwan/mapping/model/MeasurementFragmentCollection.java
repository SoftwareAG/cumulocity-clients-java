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
