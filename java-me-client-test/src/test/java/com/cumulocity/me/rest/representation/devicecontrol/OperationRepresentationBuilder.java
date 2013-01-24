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
        setObjectField("status", status);
        return this;
    }
    
    public OperationRepresentationBuilder withFailureReason(String failureReason) {
        setObjectField("failureReason", failureReason);
        return this;
    }
    
    public OperationRepresentationBuilder withCreationTime(Date creationTime) {
        setObjectField("creationTime", creationTime);
        return this;
    }
    
    public OperationRepresentationBuilder withSource(ExternalIDCollectionRepresentation deviceExternalIDs) {
        setObjectField("deviceExternalIDs", deviceExternalIDs);
        return this;
    }
    
    public OperationRepresentationBuilder withDeviceId(GId deviceId) {
        setObjectField("deviceId", deviceId);
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
