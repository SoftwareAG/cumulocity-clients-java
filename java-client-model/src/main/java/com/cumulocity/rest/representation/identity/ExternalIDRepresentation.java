package com.cumulocity.rest.representation.identity;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.svenson.JSONProperty;

import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

import javax.validation.constraints.Size;

public class ExternalIDRepresentation extends AbstractExtensibleRepresentation {

    @NotNull(operation = Command.CREATE)
    @Size(min = 1, message = "field cannot be empty")
    private String externalId;

    @NotNull(operation = Command.CREATE)
    @Size(min = 1, message = "field cannot be empty")
    private String type;

    private ManagedObjectRepresentation managedObject;

    @JSONProperty(ignoreIfNull = true)
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSONProperty(ignoreIfNull = true)
    public ManagedObjectRepresentation getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(ManagedObjectRepresentation managedObject) {
        this.managedObject = managedObject;
    }

}
