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
