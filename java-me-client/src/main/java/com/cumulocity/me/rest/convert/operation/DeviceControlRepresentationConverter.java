package com.cumulocity.me.rest.convert.operation;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationCollectionRepresentation;

public class DeviceControlRepresentationConverter extends BaseResourceRepresentationConverter {

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setOperations((OperationCollectionRepresentation) 
                getObject(json, "operations", OperationCollectionRepresentation.class));
        $(representation).setOperationsByStatus(getString(json, "operationsByStatus"));
        $(representation).setOperationsByAgentId(getString(json, "operationsByAgentId"));
        $(representation).setOperationsByAgentIdAndStatus(getString(json, "operationsByAgentIdAndStatus"));
        $(representation).setOperationsByDeviceId(getString(json, "operationsByDeviceId"));
        $(representation).setOperationsByDeviceIdAndStatus(getString(json, "operationsByDeviceIdAndStatus"));
    }

    protected Class supportedRepresentationType() {
        return DeviceControlRepresentation.class;
    }
    
    private DeviceControlRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (DeviceControlRepresentation) baseRepresentation;
    }
}
