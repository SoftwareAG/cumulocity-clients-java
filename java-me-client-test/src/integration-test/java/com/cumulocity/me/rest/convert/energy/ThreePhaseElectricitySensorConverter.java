package com.cumulocity.me.rest.convert.energy;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.model.energy.sensor.ThreePhaseElectricitySensor;

public class ThreePhaseElectricitySensorConverter extends BaseRepresentationConverter {
    
    @Override
    protected Class<?> supportedRepresentationType() {
        return ThreePhaseElectricitySensor.class;
    }

    @Override
    public JSONObject toJson(Object representation) {
        return new JSONObject();
    }

    @Override
    public Object fromJson(JSONObject json) {
        return new ThreePhaseElectricitySensor();
    }
}
