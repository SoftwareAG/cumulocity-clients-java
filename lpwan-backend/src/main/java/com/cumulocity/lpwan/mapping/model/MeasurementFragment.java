/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

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
