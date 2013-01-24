package com.cumulocity.me.rest.representation.identity;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class ExternalIDRepresentation extends BaseCumulocityResourceRepresentation {

    private String externalId;

    private String type;

    private ManagedObjectRepresentation managedObject;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ManagedObjectRepresentation getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(ManagedObjectRepresentation managedObject) {
        this.managedObject = managedObject;
    }

}
