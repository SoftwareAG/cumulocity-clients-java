package com.cumulocity.sdk.client.measurement;

import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedMeasurementCollectionRepresentation extends MeasurementCollectionRepresentation
        implements PagedCollectionRepresentation<MeasurementRepresentation> {

    private final PagedCollectionResource<MeasurementRepresentation, ? extends MeasurementCollectionRepresentation>
            collectionResource;

    public PagedMeasurementCollectionRepresentation(MeasurementCollectionRepresentation collection,
            PagedCollectionResource<MeasurementRepresentation, ? extends MeasurementCollectionRepresentation> collectionResource) {
        this.collectionResource = collectionResource;
        setMeasurements(collection.getMeasurements());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
    }

    @Override
    public Iterable<MeasurementRepresentation> allPages() {
        return new PagedCollectionIterable<MeasurementRepresentation, MeasurementCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<MeasurementRepresentation> elements(int limit) {
        return new PagedCollectionIterable<MeasurementRepresentation, MeasurementCollectionRepresentation>(
                collectionResource, this, limit);
    }
}
