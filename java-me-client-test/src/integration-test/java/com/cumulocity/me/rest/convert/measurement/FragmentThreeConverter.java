package com.cumulocity.me.rest.convert.measurement;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.measurement.FragmentThree;

public class FragmentThreeConverter extends BaseRepresentationConverter {
    
    @Override
    protected Class<?> supportedRepresentationType() {
        return FragmentThree.class;
    }

    @Override
    public JSONObject toJson(Object representation) {
        return new JSONObject();
    }

    @Override
    public Object fromJson(JSONObject json) {
        return new FragmentThree();
    }
}
