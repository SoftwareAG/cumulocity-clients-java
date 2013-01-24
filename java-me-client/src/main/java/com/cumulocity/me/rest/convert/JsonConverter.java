package com.cumulocity.me.rest.convert;

import com.cumulocity.me.rest.json.JSONObject;

public interface JsonConverter {

    boolean supports(Class representationType);
    
    JSONObject toJson(Object representation);
    
    Object fromJson(JSONObject json);
}
