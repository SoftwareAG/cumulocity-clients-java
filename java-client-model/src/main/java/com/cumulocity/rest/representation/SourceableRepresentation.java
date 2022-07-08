package com.cumulocity.rest.representation;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public interface SourceableRepresentation {

    ManagedObjectRepresentation getSource();

    void setSource(ManagedObjectRepresentation managedObject);
}
