package com.cumulocity.rest.representation.tenant;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class TenantCollectionRepresentation extends BaseCollectionRepresentation<TenantRepresentation> {

    private List<TenantRepresentation> tenants;

    public List<TenantRepresentation> getTenants() {
        return tenants;
    }

    @JSONTypeHint(TenantRepresentation.class)
    public void setTenants(List<TenantRepresentation> tenants) {
        this.tenants = tenants;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<TenantRepresentation> iterator() {
        return tenants.iterator();
    }
}
