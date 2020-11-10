package com.cumulocity.sdk.client;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.SourceableRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class SourceableUtils {

    public static void keepOnlySourceId(SourceableRepresentation representation) {
        if (representation.getSource() != null) {
            final GId id = representation.getSource().getId();
            final ManagedObjectRepresentation optimizedSource = new ManagedObjectRepresentation();
            optimizedSource.setId(id);
            representation.setSource(optimizedSource);
        }
    }
}
