/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
