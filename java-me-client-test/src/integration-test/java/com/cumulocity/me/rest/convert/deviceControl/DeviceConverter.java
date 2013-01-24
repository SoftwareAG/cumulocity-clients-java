package com.cumulocity.me.rest.convert.deviceControl;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;

public class DeviceConverter extends BaseRepresentationConverter {

    @Override
    protected Class<?> supportedRepresentationType() {
        //return Device.class;
        return null;
    }

    @Override
    public JSONObject toJson(Object representation) {
        return new JSONObject();
    }

    @Override
    public Object fromJson(JSONObject json) {
        //return new Device();
        return null;
    }
    
}
