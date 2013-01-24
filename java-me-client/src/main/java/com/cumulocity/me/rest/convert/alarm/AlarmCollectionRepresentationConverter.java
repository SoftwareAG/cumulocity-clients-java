package com.cumulocity.me.rest.convert.alarm;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;

public class AlarmCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {
    
    private static final String PROP_ALARMS = "alarms";

    protected Class supportedRepresentationType() {
        return AlarmCollectionRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_ALARMS, $(representation).getAlarms());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setAlarms(getList(json, PROP_ALARMS, AlarmRepresentation.class));
    }

    private AlarmCollectionRepresentation $(BaseCumulocityResourceRepresentation representation) {
        return (AlarmCollectionRepresentation) representation;
    }
}
