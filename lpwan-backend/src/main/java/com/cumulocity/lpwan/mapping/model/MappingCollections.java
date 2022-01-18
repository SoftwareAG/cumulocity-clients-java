/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.mapping.model;

import lombok.Data;

@Data
public class MappingCollections {
    
    private MeasurementFragmentCollection measurementFragments;
    private EventFragmentCollection eventFragments;
    private EventMappingCollection eventMappings;
    private ManagedObjectFragmentCollection managedObjectFragments;
    private AlarmMappingCollection alarmMappings;
    
    public MappingCollections() {
        measurementFragments = new MeasurementFragmentCollection();
        eventFragments = new EventFragmentCollection();
        eventMappings = new EventMappingCollection();
        managedObjectFragments = new ManagedObjectFragmentCollection();
        alarmMappings = new AlarmMappingCollection();
    }
    
}
