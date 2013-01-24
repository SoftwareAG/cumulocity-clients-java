package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.Coordinate;

public class CoordinateConverter extends BaseRepresentationConverter {
    
    private static final String PROP_LONGITUDE = "longitude";
    private static final String PROP_LATITUDE = "latitude";
    
    @Override
    protected Class<?> supportedRepresentationType() {
        return Coordinate.class;
    }

    @Override
    public JSONObject toJson(Object object) {
        JSONObject json = new JSONObject();
        Coordinate representation = (Coordinate) object;
        putDoubleObj(json, PROP_LONGITUDE, representation.getLongitude());
        putDoubleObj(json, PROP_LATITUDE, representation.getLatitude());
        return json;
    }

    @Override
    public Object fromJson(JSONObject json) {
        Coordinate representation = new Coordinate();
        representation.setLongitude(getDoubleObj(json, PROP_LONGITUDE));
        representation.setLatitude(getDoubleObj(json, PROP_LATITUDE));
        return representation;
    }
}
