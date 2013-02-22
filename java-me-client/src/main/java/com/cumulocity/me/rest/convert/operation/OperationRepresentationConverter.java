/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.rest.convert.operation;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.ResourceRepresentation;
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

    protected void instanceToJson(BaseResourceRepresentation representation, JSONObject json) {
        putGId(json, $(representation).getId());
        putGId(json, PROP_DEVICE_ID, $(representation).getDeviceId());
        putString(json, PROP_STATUS, $(representation).getStatus());
        putString(json, PROP_FAILURE_REASON, $(representation).getFailureReason());
        putDate(json, PROP_CREATION_TIME, $(representation).getCreationTime());
        putObject(json, PROP_DEVICE_EXTERNAL_IDS, $(representation).getDeviceExternalIDs());
    }

    protected void instanceFromJson(JSONObject json, BaseResourceRepresentation representation) {
        $(representation).setId(getGId(json));
        $(representation).setDeviceId(getGId(json, PROP_DEVICE_ID));
        $(representation).setStatus(getString(json, PROP_STATUS));
        $(representation).setFailureReason(getString(json, PROP_FAILURE_REASON));
        $(representation).setCreationTime(getDate(json, PROP_CREATION_TIME));
        $(representation).setDeviceExternalIDs(
                (ExternalIDCollectionRepresentation) getObject(json, PROP_DEVICE_EXTERNAL_IDS, ExternalIDCollectionRepresentation.class));
    }

    public boolean isValid(ResourceRepresentation representation, RepresentationValidationContext context) {
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

    private OperationRepresentation $(ResourceRepresentation baseRepresentation) {
        return (OperationRepresentation) baseRepresentation;
    }

}
