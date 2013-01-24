package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.PageStatisticsRepresentation;

public class EmptyPagedCollectionResource<T extends BaseCollectionRepresentation> implements PagedCollectionResource<T> {

    private final Class<T> type;

    public EmptyPagedCollectionResource(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() throws SDKException {
        T collectionRepresentaton = newCollectionRepresentationInstance();
        collectionRepresentaton.setPageStatistics(new PageStatisticsRepresentation());
        return collectionRepresentaton;
    }

    @Override
    public T getPage(BaseCollectionRepresentation collectionRepresentation, int pageNumber) throws SDKException {
        return pageNumber == 1 ? get() : null;
    }

    @Override
    public T getNextPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        return null;
    }

    @Override
    public T getPreviousPage(BaseCollectionRepresentation collectionRepresentation) throws SDKException {
        return null;
    }

    private T newCollectionRepresentationInstance() throws SDKException {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new SDKException("internal error", ex);
        }
    }
}
