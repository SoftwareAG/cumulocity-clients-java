package com.cumulocity.me.sdk.client.page;

import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.PageStatisticsRepresentation;
import com.cumulocity.me.sdk.SDKException;

public class EmptyPagedCollectionResource implements PagedCollectionResource {

    private final Class type;

    public EmptyPagedCollectionResource(Class type) {
        this.type = type;
    }

    public CumulocityResourceRepresentation get() {
    	BaseCollectionRepresentation collectionRepresentaton = (BaseCollectionRepresentation) newCollectionRepresentationInstance();
    	collectionRepresentaton.setPageStatistics(new PageStatisticsRepresentation());
        return collectionRepresentaton;
    }

    public Object getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) {
        return pageNumber == 1 ? get() : null;
    }

    public Object getNextPage(BaseCollectionRepresentation collectionRepresentation) {
        return null;
    }

    public Object getPreviousPage(BaseCollectionRepresentation collectionRepresentation) {
        return null;
    }

    private Object newCollectionRepresentationInstance() {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new SDKException("internal error", ex);
        }
    }
}
