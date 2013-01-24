package com.cumulocity.me.rest.representation.identity;

import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class ExternalIDRepresentationBuilder extends AbstractObjectBuilder<ExternalIDRepresentation> {

    public static final ExternalIDRepresentationBuilder aExternalIDRepresentation() {
        return new ExternalIDRepresentationBuilder();
    }
    
    public ExternalIDRepresentationBuilder withExternalId(String externalId) {
        setObjectField("externalId", externalId);
        return this;
    }
    
    public ExternalIDRepresentationBuilder withType(String type) {
        setObjectField("type", type);
        return this;
    }
    
    public ExternalIDRepresentationBuilder withManagedObject(ManagedObjectRepresentation managedObject) {
        setObjectField("managedObject", managedObject);
        return this;
    }
    
    @Override
    protected ExternalIDRepresentation createDomainObject() {
        return new ExternalIDRepresentation();
    }
}
