package com.cumulocity.sdk.client.audit;

import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedAuditCollectionRepresentation extends AuditRecordCollectionRepresentation
        implements PagedCollectionRepresentation<AuditRecordRepresentation> {

    private final PagedCollectionResource<AuditRecordRepresentation, ? extends AuditRecordCollectionRepresentation>
            collectionResource;

    public PagedAuditCollectionRepresentation(AuditRecordCollectionRepresentation collection,
            PagedCollectionResource<AuditRecordRepresentation, ? extends AuditRecordCollectionRepresentation> collectionResource) {
        this.collectionResource = collectionResource;
        setAuditRecords(collection.getAuditRecords());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
    }

    @Override
    public Iterable<AuditRecordRepresentation> allPages() {
        return new PagedCollectionIterable<AuditRecordRepresentation, AuditRecordCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<AuditRecordRepresentation> elements(int limit) {
        return new PagedCollectionIterable<AuditRecordRepresentation, AuditRecordCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
