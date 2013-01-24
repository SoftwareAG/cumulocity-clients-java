package com.cumulocity.me.rest.convert.deviceControl;

import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.model.control.Relay;
import com.cumulocity.model.control.Relay.RelayState;

public class RelayConverter extends BaseRepresentationConverter {

    private static final String PROP_STATE = "state";
    
    public JSONObject toJson(Object representation) {
        JSONObject json = new JSONObject();
        Relay relayControl = (Relay) representation;
        putString(json, PROP_STATE, relayControl.getRelayState().name());
        return json;
    }

    public Object fromJson(JSONObject json) {
        Relay relayControl = new Relay();
        relayControl.setRelayState(RelayState.OPEN);
        return relayControl;
    }

    protected Class supportedRepresentationType() {
        return Relay.class;
    }

}
