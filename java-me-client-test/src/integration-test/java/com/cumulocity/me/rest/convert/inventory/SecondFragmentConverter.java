package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.SecondFragment;

public class SecondFragmentConverter extends BaseRepresentationConverter {
    
    @Override
    protected Class<?> supportedRepresentationType() {
        return SecondFragment.class;
    }

    @Override
    public JSONObject toJson(Object representation) {
        return new JSONObject();
    }

    @Override
    public Object fromJson(JSONObject json) {
        return new SecondFragment();
    }
}
