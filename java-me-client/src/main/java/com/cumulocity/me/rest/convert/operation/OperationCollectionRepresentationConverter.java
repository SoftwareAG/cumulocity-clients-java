package com.cumulocity.me.rest.convert.operation;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;

public class OperationCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {

    private static final String PROP_OPERATIONS = "operations";

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_OPERATIONS, $(representation).getOperations());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setOperations(getList(json, PROP_OPERATIONS, OperationRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return OperationCollectionRepresentation.class;
    }

    private OperationCollectionRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (OperationCollectionRepresentation) baseRepresentation;
    }
}
