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
package com.cumulocity.me.rest.representation.operation;

import java.util.Date;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;

public class OperationRepresentation extends AbstractExtensibleRepresentation {

    private GId id;
    
    private GId deviceId;

    private String status;
    
    private String failureReason;

    private Date creationTime;

    private ExternalIDCollectionRepresentation deviceExternalIDs;

    public GId getId() {
        return id;
    }

    public void setId(GId id) {
        this.id = id;
    }

    public GId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(GId deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(final String failureReason) {
        this.failureReason = failureReason;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public ExternalIDCollectionRepresentation getDeviceExternalIDs() {
        return deviceExternalIDs;
    }

    public void setDeviceExternalIDs(ExternalIDCollectionRepresentation deviceExternalIDs) {
        this.deviceExternalIDs = deviceExternalIDs;
    }
}