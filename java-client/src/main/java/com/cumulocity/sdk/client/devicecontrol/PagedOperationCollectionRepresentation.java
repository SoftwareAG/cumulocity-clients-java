package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedOperationCollectionRepresentation extends OperationCollectionRepresentation
        implements PagedCollectionRepresentation<OperationRepresentation> {

    private final PagedCollectionResource<OperationRepresentation, ? extends OperationCollectionRepresentation>
            collectionResource;

    public PagedOperationCollectionRepresentation(OperationCollectionRepresentation collection,
            PagedCollectionResource<OperationRepresentation, ? extends OperationCollectionRepresentation> collectionResource) {
        setOperations(collection.getOperations());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        setNext(collection.getNext());
        setPrev(collection.getPrev());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<OperationRepresentation> allPages() {
        return new PagedCollectionIterable<OperationRepresentation, OperationCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<OperationRepresentation> elements(int limit) {
        return new PagedCollectionIterable<OperationRepresentation, OperationCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
