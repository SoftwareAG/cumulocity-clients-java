package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;

public class ManagedObjectReferenceCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {

    public static final String PROP_REFERENCES = "references";

    protected Class supportedRepresentationType() {
        return ManagedObjectReferenceCollectionRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_REFERENCES, $(representation).getReferences());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setReferences(getList(json, PROP_REFERENCES, ManagedObjectReferenceRepresentation.class));
    }

    private ManagedObjectReferenceCollectionRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (ManagedObjectReferenceCollectionRepresentation) baseRepresentation;
    }

}
