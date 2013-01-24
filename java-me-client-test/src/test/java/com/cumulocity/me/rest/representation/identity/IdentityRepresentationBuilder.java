package com.cumulocity.me.rest.representation.identity;

import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class IdentityRepresentationBuilder extends AbstractObjectBuilder<IdentityRepresentation> {

    public static final IdentityRepresentationBuilder aIdentityRepresentation() {
        return new IdentityRepresentationBuilder();
    }
    
    public IdentityRepresentationBuilder withExternalIdsOfGlobalId(String externalIdsOfGlobalId) {
        setObjectField("externalIdsOfGlobalId", externalIdsOfGlobalId);
        return this;
    }
    
    public IdentityRepresentationBuilder withExternalId(String externalId) {
        setObjectField("externalId", externalId);
        return this;
    }
    
    @Override
    protected IdentityRepresentation createDomainObject() {
        return new IdentityRepresentation();
    }
}
