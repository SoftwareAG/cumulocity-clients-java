package com.cumulocity.lpwan.payload.uplink.model;

import lombok.Data;

@Data
public class EventMapping {
    
    private String type;
    private String text;
    private String fragmentType; //optional
    private String innerType; //optional
    
}
