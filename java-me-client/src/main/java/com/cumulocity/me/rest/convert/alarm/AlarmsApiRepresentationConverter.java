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
package com.cumulocity.me.rest.convert.alarm;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmsApiRepresentation;

public class AlarmsApiRepresentationConverter extends BaseResourceRepresentationConverter {
    
    protected Class supportedRepresentationType() {
        return AlarmsApiRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setAlarms((AlarmCollectionRepresentation) getObject(json, "alarms", AlarmCollectionRepresentation.class));
        $(representation).setAlarmsForStatus(getString(json, "alarmsForStatus"));
        $(representation).setAlarmsForSource(getString(json, "alarmsForSource"));
        $(representation).setAlarmsForSourceAndStatus(getString(json, "alarmsForSourceAndStatus"));
        $(representation).setAlarmsForTime(getString(json, "alarmsForTime"));
        $(representation).setAlarmsForStatusAndTime(getString(json, "alarmsForStatusAndTime"));
        $(representation).setAlarmsForSourceAndTime(getString(json, "alarmsForSourceAndTime"));
        $(representation).setAlarmsForSourceAndStatusAndTime(getString(json, "alarmsForSourceAndStatusAndTime"));
    }

    private AlarmsApiRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (AlarmsApiRepresentation) baseRepresentation;
    }
}
