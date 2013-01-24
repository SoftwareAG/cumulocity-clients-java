package com.cumulocity.me.rest.convert.operation;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class OperationRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {

    private static final String PROP_DEVICE_ID = "deviceId";
    private static final String PROP_STATUS = "status";
    private static final String PROP_FAILURE_REASON = "failureReason";
    private static final String PROP_CREATION_TIME = "creationTime";
    private static final String PROP_DEVICE_EXTERNAL_IDS = "deviceExternalIDs";

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putGId(json, $(representation).getId());
        putGId(json, PROP_DEVICE_ID, $(representation).getDeviceId());
        putString(json, PROP_STATUS, $(representation).getStatus());
        putString(json, PROP_FAILURE_REASON, $(representation).getFailureReason());
        putDate(json, PROP_CREATION_TIME, $(representation).getCreationTime());
        putObject(json, PROP_DEVICE_EXTERNAL_IDS, $(representation).getDeviceExternalIDs());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setId(getGId(json));
        $(representation).setDeviceId(getGId(json, PROP_DEVICE_ID));
        $(representation).setStatus(getString(json, PROP_STATUS));
        $(representation).setFailureReason(getString(json, PROP_FAILURE_REASON));
        $(representation).setCreationTime(getDate(json, PROP_CREATION_TIME));
        $(representation).setDeviceExternalIDs(
                (ExternalIDCollectionRepresentation) getObject(json, PROP_DEVICE_EXTERNAL_IDS, ExternalIDCollectionRepresentation.class));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNotNull(PROP_DEVICE_ID, $(representation).getDeviceId());
            assertNull(PROP_STATUS, $(representation).getStatus());
            assertNull(PROP_FAILURE_REASON, $(representation).getFailureReason());
            assertNull(PROP_CREATION_TIME, $(representation).getCreationTime());
            assertNull(PROP_DEVICE_EXTERNAL_IDS, $(representation).getDeviceExternalIDs());
        }
        if (CommandBasedRepresentationValidationContext.UPDATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNull(PROP_DEVICE_ID, $(representation).getDeviceId());
            assertNotNull(PROP_STATUS, $(representation).getStatus());
            assertNull(PROP_CREATION_TIME, $(representation).getCreationTime());
            assertNull(PROP_DEVICE_EXTERNAL_IDS, $(representation).getDeviceExternalIDs());
        }
        return true;
    }

    protected Class supportedRepresentationType() {
        return OperationRepresentation.class;
    }

    private OperationRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (OperationRepresentation) baseRepresentation;
    }

}
