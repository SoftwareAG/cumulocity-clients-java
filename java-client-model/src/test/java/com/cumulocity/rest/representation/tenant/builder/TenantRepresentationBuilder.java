package com.cumulocity.rest.representation.tenant.builder;

import com.cumulocity.rest.representation.tenant.TenantRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class TenantRepresentationBuilder extends AbstractObjectBuilder<TenantRepresentation> {

    public TenantRepresentationBuilder withId(String id) {
        setFieldValue("id", id);
        return this;
    }

    @Override
    protected TenantRepresentation createDomainObject() {
        return new TenantRepresentation();
    }
}
