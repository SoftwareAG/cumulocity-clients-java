/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.identity;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityMediaType;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class ExternalIDCollectionImpl extends PagedCollectionResourceImpl implements
        PagedCollectionResource {

    public ExternalIDCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }

    protected CumulocityMediaType getMediaType() {
        return IdentityMediaType.EXTERNAL_ID_COLLECTION;
    }

    protected Class getResponseClass() {
        return ExternalIDCollectionRepresentation.class;
    }
}
