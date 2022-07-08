package com.cumulocity.rest.representation.tenant;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class TenantReferenceRepresentation extends AbstractExtensibleRepresentation {

    private TenantRepresentation tenant;

    public TenantRepresentation getTenant() {
        return tenant;
    }

    public void setTenant(TenantRepresentation tenant) {
        this.tenant = tenant;
    }
}
