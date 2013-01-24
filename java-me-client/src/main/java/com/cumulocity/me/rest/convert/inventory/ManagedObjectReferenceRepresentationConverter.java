package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class ManagedObjectReferenceRepresentationConverter extends BaseResourceRepresentationConverter {

    public static final String PROP_MANAGED_OBJECT = "managedObject";

    protected Class supportedRepresentationType() {
        return ManagedObjectReferenceRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putObject(json, PROP_MANAGED_OBJECT, $(representation).getManagedObject());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setManagedObject((ManagedObjectRepresentation) getObject(json, PROP_MANAGED_OBJECT, ManagedObjectRepresentation.class));
    }

    private ManagedObjectReferenceRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (ManagedObjectReferenceRepresentation) baseRepresentation;
    }

}
