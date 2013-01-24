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