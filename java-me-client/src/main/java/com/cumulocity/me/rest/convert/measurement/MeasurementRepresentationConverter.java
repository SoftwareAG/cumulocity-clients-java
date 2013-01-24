package com.cumulocity.me.rest.convert.measurement;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class MeasurementRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {
    
    private static final String PROP_TYPE = "type";
    private static final String PROP_TIME = "time";
    private static final String PROP_SOURCE = "source";

    protected Class supportedRepresentationType() {
        return MeasurementRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putGId(json, $(representation).getId());
        putString(json, PROP_TYPE, $(representation).getType());
        putDate(json, PROP_TIME, $(representation).getTime());
        putObject(json, PROP_SOURCE, $(representation).getSource());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setId(getGId(json));
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setTime(getDate(json, PROP_TIME));
        $(representation).setSource((ManagedObjectRepresentation) getObject(json, PROP_SOURCE, ManagedObjectRepresentation.class));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNotNull(PROP_TYPE, $(representation).getType());
            assertNotNull(PROP_TIME, $(representation).getTime());
            assertNotNull(PROP_SOURCE, $(representation).getSource());
        }
        return true;
    }

    private MeasurementRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (MeasurementRepresentation) baseRepresentation;
    }
}
