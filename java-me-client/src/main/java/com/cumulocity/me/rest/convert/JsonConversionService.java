package com.cumulocity.me.rest.convert;

import com.cumulocity.me.rest.json.JSONObject;

public interface JsonConversionService {
    
    void register(JsonConverter converter);

    JSONObject toJson(Object representation);
    
    Object fromJson(JSONObject json, Class expectedType);
}
