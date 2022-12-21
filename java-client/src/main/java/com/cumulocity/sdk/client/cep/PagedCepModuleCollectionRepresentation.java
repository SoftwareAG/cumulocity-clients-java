package com.cumulocity.sdk.client.cep;

import com.cumulocity.rest.representation.cep.CepModuleCollectionRepresentation;
import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PagedCollectionIterable;
import com.cumulocity.sdk.client.PagedCollectionRepresentation;
import com.cumulocity.sdk.client.PagedCollectionResource;

public class PagedCepModuleCollectionRepresentation  extends CepModuleCollectionRepresentation
implements PagedCollectionRepresentation<CepModuleRepresentation>  {
    private final PagedCollectionResource<CepModuleRepresentation, ? extends CepModuleCollectionRepresentation> collectionResource;

    public PagedCepModuleCollectionRepresentation(CepModuleCollectionRepresentation collection,
            PagedCollectionResource<CepModuleRepresentation, ? extends CepModuleCollectionRepresentation> collectionResource) {
        setModules(collection.getModules());
        setPageStatistics(collection.getPageStatistics());
        setSelf(collection.getSelf());
        setNext(collection.getNext());
        setPrev(collection.getPrev());
        this.collectionResource = collectionResource;
    }

    @Override
    public Iterable<CepModuleRepresentation> allPages() {
        return new PagedCollectionIterable<CepModuleRepresentation, CepModuleCollectionRepresentation>(
                collectionResource, this);
    }

    @Override
    public Iterable<CepModuleRepresentation> elements(int limit) {
        return new PagedCollectionIterable<CepModuleRepresentation, CepModuleCollectionRepresentation>(
                collectionResource, this, limit);
    }

}
