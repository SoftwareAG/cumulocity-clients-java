package com.cumulocity.sdk.client.option;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.tenant.OptionCollectionRepresentation;
import com.cumulocity.rest.representation.tenant.OptionMediaType;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class TenantOptionCollectionImpl extends PagedCollectionResourceImpl<OptionRepresentation, OptionCollectionRepresentation, PagedTenantOptionCollectionRepresentation>
        implements TenantOptionCollection {

    public TenantOptionCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return OptionMediaType.OPTION_COLLECTION;
    }

    @Override
    protected Class<OptionCollectionRepresentation> getResponseClass() {
        return OptionCollectionRepresentation.class;
    }

    @Override
    protected PagedTenantOptionCollectionRepresentation wrap(OptionCollectionRepresentation collection) {
        return new PagedTenantOptionCollectionRepresentation(collection, this);
    }
}
