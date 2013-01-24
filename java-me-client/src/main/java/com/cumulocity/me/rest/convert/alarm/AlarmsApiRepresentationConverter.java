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
