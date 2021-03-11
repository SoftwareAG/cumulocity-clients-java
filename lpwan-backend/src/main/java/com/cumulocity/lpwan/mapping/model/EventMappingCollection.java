package com.cumulocity.lpwan.mapping.model;

import java.util.ArrayList;
import java.util.List;

import com.cumulocity.lpwan.payload.uplink.model.EventMapping;

import lombok.Data;

@Data
public class EventMappingCollection {

    private List<EventMapping> collection = new ArrayList<>();
    
    public void add(EventMapping eventMapping) {
        collection.add(eventMapping);
    }
    
    public List<EventMapping> get() {
        return collection;
    }
}
