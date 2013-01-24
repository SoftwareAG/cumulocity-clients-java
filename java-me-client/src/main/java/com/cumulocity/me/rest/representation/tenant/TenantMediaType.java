package com.cumulocity.me.rest.representation.tenant;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

/**
 * We follow here convention from {@link MediaType} class, where we have both {@link MediaType}
 * instances, and string representations (with '_TYPE' suffix in name).
 */
public class TenantMediaType extends BaseCumulocityMediaType {

    public static final TenantMediaType TENANT = new TenantMediaType("tenant");

    public static final String TENANT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "tenant+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final TenantMediaType TENANT_COLLECTION = new TenantMediaType("tenantCollection");

    public static final String TENANT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "tenantCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public TenantMediaType(String entity) {
        super(entity);
    }
}
