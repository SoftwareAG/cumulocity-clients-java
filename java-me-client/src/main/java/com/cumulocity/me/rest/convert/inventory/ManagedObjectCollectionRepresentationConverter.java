package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class ManagedObjectCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {

    public static final String PROP_MANAGED_OBJECTS = "managedObjects";

    protected Class supportedRepresentationType() {
        return ManagedObjectCollectionRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_MANAGED_OBJECTS, $(representation).getManagedObjects());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setManagedObjects(getList(json, PROP_MANAGED_OBJECTS, ManagedObjectRepresentation.class));
    }

    private ManagedObjectCollectionRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (ManagedObjectCollectionRepresentation) baseRepresentation;
    }
}
