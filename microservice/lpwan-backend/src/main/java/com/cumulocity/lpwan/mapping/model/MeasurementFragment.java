package com.cumulocity.lpwan.mapping.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class MeasurementFragment {
    
    private String type;
    
    private Map<String, Object> seriesObject = new HashMap<>();

    public void putFragmentValue(String key, DecodedObject decodedObject) {
        seriesObject.put(key, decodedObject.getFields());
    }
    
    public Object getFragmentValue(String key) {
        return seriesObject.get(key);
    }
}
