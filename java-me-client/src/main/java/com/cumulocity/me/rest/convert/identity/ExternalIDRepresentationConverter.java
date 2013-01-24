package com.cumulocity.me.rest.convert.identity;

import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class ExternalIDRepresentationConverter extends BaseResourceRepresentationConverter {

    public static final String PROP_TYPE = "type";
    public static final String PROP_EXTERNAL_ID = "externalId";
    public static final String PROP_MANAGED_OBJECT = "managedObject";

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putString(json, PROP_TYPE, $(representation).getType());
        putString(json, PROP_EXTERNAL_ID, $(representation).getExternalId());
        putObject(json, PROP_MANAGED_OBJECT, $(representation).getManagedObject());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setExternalId(getString(json, PROP_EXTERNAL_ID));
        $(representation).setManagedObject((ManagedObjectRepresentation)getObject(json, PROP_MANAGED_OBJECT, ManagedObjectRepresentation.class));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNotNull(PROP_TYPE, $(representation).getType());
            assertNotNull(PROP_EXTERNAL_ID, $(representation).getExternalId());
        }
        return true;
    }
    
    protected Class supportedRepresentationType() {
        return ExternalIDRepresentation.class;
    }
    
    private ExternalIDRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (ExternalIDRepresentation) baseRepresentation;
    }

}
