package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.model.audit.Change;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;

public class ChangeConverter extends BaseRepresentationConverter {

    private static final String PROP_ATTRIBUTE = "attribute";
    private static final String PROP_TYPE = "type";
    private static final String PROP_PREVIOUS_VALUE = "previousValue";
    private static final String PROP_NEW_VALUE = "newValue";
    
    public JSONObject toJson(Object representation) {
        JSONObject json = new JSONObject();
        json.putOpt(PROP_ATTRIBUTE, ((Change)representation).getAttribute());
        json.putOpt(PROP_TYPE, ((Change)representation).getType());
        json.putOpt(PROP_PREVIOUS_VALUE, (getConversionService().toJson(((Change)representation).getPreviousValue())));
        json.putOpt(PROP_NEW_VALUE, (getConversionService().toJson(((Change)representation).getNewValue())));
        return json;
    }

    public Object fromJson(JSONObject json) {
        Change change = new Change();
        change.setAttribute(getString(json, PROP_ATTRIBUTE));
        change.setType(getString(json, PROP_TYPE));
        change.setPreviousValue(getObject(json, PROP_PREVIOUS_VALUE, Change.class));
        change.setNewValue(getObject(json, PROP_NEW_VALUE, Change.class));
        return change;
    }

    protected Class supportedRepresentationType() {
        return Change.class;
    }
}
