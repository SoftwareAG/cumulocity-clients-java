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
