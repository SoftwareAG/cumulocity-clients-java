package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONException;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class ManagedObjectRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {

    public static final String PROP_TYPE = "type";
    public static final String PROP_NAME = "name";
    public static final String PROP_CHILD_DEVICES = "childDevices";
    public static final String PROP_CHILD_ASSETS = "childAssets";
    public static final String PROP_PARENTS = "parents";
    public static final String PROP_LAST_UPDATED = "lastUpdated";

    protected Class supportedRepresentationType() {
        return ManagedObjectRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) throws JSONException {
        putGId(json, $(representation).getId());
        putString(json, PROP_TYPE, $(representation).getType());
        putString(json, PROP_NAME, $(representation).getName());
        putObject(json, PROP_CHILD_DEVICES, $(representation).getChildDevices());
        putObject(json, PROP_CHILD_ASSETS, $(representation).getChildAssets());
        putObject(json, PROP_PARENTS, $(representation).getParents());
        putDate(json, PROP_LAST_UPDATED, $(representation).getLastUpdated());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) throws JSONException {
        $(representation).setId(getGId(json));
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setName(getString(json, PROP_NAME));
        $(representation).setChildAssets((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_CHILD_ASSETS,
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setChildDevices((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_CHILD_DEVICES, 
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setParents((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_PARENTS,
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setLastUpdated(getDate(json, PROP_LAST_UPDATED));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context) ||
                CommandBasedRepresentationValidationContext.UPDATE.equals(context)) {
           
            assertNull(PROP_ID, $(representation).getId());
            assertNull(PROP_LAST_UPDATED, $(representation).getLastUpdated());
        }
        return true;
    }

    private ManagedObjectRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (ManagedObjectRepresentation) baseRepresentation;
    }
}
