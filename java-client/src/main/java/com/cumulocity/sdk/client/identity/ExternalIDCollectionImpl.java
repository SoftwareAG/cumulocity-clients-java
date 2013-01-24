/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.identity;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.IdentityMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class ExternalIDCollectionImpl extends PagedCollectionResourceImpl<ExternalIDCollectionRepresentation> implements
        PagedCollectionResource<ExternalIDCollectionRepresentation> {

    @Deprecated
    public ExternalIDCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public ExternalIDCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    @Override
    protected CumulocityMediaType getMediaType() {
        return IdentityMediaType.EXTERNAL_ID_COLLECTION;
    }

    @Override
    protected Class<ExternalIDCollectionRepresentation> getResponseClass() {
        return ExternalIDCollectionRepresentation.class;
    }
}
