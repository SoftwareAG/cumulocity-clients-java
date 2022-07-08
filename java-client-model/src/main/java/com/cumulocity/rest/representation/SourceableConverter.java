package com.cumulocity.rest.representation;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.svenson.converter.TypeConverter;

public class SourceableConverter implements TypeConverter {

    @Override
    public Object fromJSON(Object in) {
        return in;
    }

    @Override
    public Object toJSON(Object in) {
        if (in == null) {
            return null;
        } else if (in instanceof ManagedObjectRepresentation) {
            return keepOnlyNecessaryFields((ManagedObjectRepresentation) in);
        }
        return in;
    }

    public static ManagedObjectRepresentation keepOnlyNecessaryFields(ManagedObjectRepresentation source) {
        final GId id = source.getId();
        final String self = source.getSelf();
        final String name = source.getName();
        final ManagedObjectRepresentation optimizedSource = new ManagedObjectRepresentation();
        optimizedSource.setId(id);
        optimizedSource.setSelf(self);
        optimizedSource.setName(name);
        return optimizedSource;
    }
}
