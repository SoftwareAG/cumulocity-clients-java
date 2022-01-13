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

import lombok.Data;
import lombok.Singular;

@Data
public class ManagedObjectFragmentCollection {

    @Singular
    private Map<String, ManagedObjectFragment> managedObjectMappingsByType = new HashMap<String, ManagedObjectFragment>();
    
    public void put(String key, ManagedObjectFragment value) {
        managedObjectMappingsByType.put(key, value);
    }
    
    public ManagedObjectFragment get(String key) {
        return managedObjectMappingsByType.get(key);
    }
    
    public Set<Map.Entry<String, ManagedObjectFragment>> entrySet() {
        return managedObjectMappingsByType.entrySet();
    }
}
