package com.cumulocity.me.rest.representation;

import com.cumulocity.me.lang.Map;

public interface CumulocityMediaType {
    
    String getTypeString();

    String getType();
    
    String getSubtype();
    
    Map getParameters();
}
