package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;

public class InventoryRepresentationConverter extends BaseResourceRepresentationConverter {

    protected Class supportedRepresentationType() {
        return InventoryRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setManagedObjectsForType(getString(json, "managedObjectsForType"));
        $(representation).setManagedObjectsForFragmentType(getString(json, "managedObjectsForFragmentType"));
        $(representation).setManagedObjectsForListOfIds(getString(json, "managedObjectsForListOfIds"));
        $(representation).setManagedObjects((ManagedObjectReferenceCollectionRepresentation) 
                getObject(json, "managedObjects", ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setSelf(getString(json, "self"));
    }

    private InventoryRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (InventoryRepresentation) baseRepresentation;
    }

}
