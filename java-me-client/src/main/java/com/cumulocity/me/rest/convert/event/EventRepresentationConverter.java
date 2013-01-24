package com.cumulocity.me.rest.convert.event;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.event.EventRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class EventRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {
    
    private static final String PROP_TYPE = "type";
    private static final String PROP_TIME = "time";
    private static final String PROP_CREATION_TIME = "creationTime";
    private static final String PROP_TEXT = "text";
    private static final String PROP_SOURCE = "source";

    protected Class supportedRepresentationType() {
        return EventRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putGId(json, $(representation).getId());
        putString(json, PROP_TYPE, $(representation).getType());
        putDate(json, PROP_TIME, $(representation).getTime());
        putDate(json, PROP_CREATION_TIME, $(representation).getCreationTime());
        putString(json, PROP_TEXT, $(representation).getText());
        putObject(json, getSourcePropertyName(), $(representation).getSource());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setId(getGId(json));
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setTime(getDate(json, PROP_TIME));
        $(representation).setCreationTime(getDate(json, PROP_CREATION_TIME));
        $(representation).setText(getString(json, PROP_TEXT));
        $(representation).setSource((ManagedObjectRepresentation) getObject(json, getSourcePropertyName(), ManagedObjectRepresentation.class));
    }
    
    protected String getSourcePropertyName() {
        return PROP_SOURCE;
    }
    
    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNotNull(PROP_TYPE, $(representation).getType());
            assertNotNull(PROP_TIME, $(representation).getTime());
            assertNull(PROP_CREATION_TIME, $(representation).getCreationTime());
            assertNotNull(PROP_TEXT, $(representation).getText());
        }
        if (CommandBasedRepresentationValidationContext.UPDATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNull(PROP_TYPE, $(representation).getType());
            assertNull(PROP_TIME, $(representation).getTime());
            assertNull(PROP_CREATION_TIME, $(representation).getCreationTime());
        }
        return true;
    }

    private EventRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (EventRepresentation) baseRepresentation;
    }
}
