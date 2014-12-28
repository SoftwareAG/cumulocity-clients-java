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
package com.cumulocity.me.rest.representation.devicecontrol;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;

public class OperationRepresentationBuilder extends BaseRepresentationBuilder<OperationRepresentation, OperationRepresentationBuilder> {

    private final Set<Object> dynamicProperties = new HashSet<Object>();
    
    public static final OperationRepresentationBuilder aOperationRepresentation() {
        return new OperationRepresentationBuilder();
    }
    
    public OperationRepresentationBuilder withStatus(String status) {
        setFieldValue("status", status);
        return this;
    }
    
    public OperationRepresentationBuilder withFailureReason(String failureReason) {
        setFieldValue("failureReason", failureReason);
        return this;
    }
    
    public OperationRepresentationBuilder withCreationTime(Date creationTime) {
        setFieldValue("creationTime", creationTime);
        return this;
    }
    
    public OperationRepresentationBuilder withSource(ExternalIDCollectionRepresentation deviceExternalIDs) {
        setFieldValue("deviceExternalIDs", deviceExternalIDs);
        return this;
    }
    
    public OperationRepresentationBuilder withDeviceId(GId deviceId) {
        setFieldValue("deviceId", deviceId);
        return this;
    }
    
    @Override
    protected OperationRepresentation createDomainObject() {
        OperationRepresentation operation = new OperationRepresentation();
        for (Object object : dynamicProperties) {
            operation.set(object);
        }
        return operation;
    } 
    
    public OperationRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }
}
