package com.cumulocity.sdk.client;

import java.util.Iterator;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class PagedCollectionIterable<T, C extends BaseCollectionRepresentation<T>> implements Iterable<T>, Iterator<T> {

    private final PagedCollectionResource<T, ? extends C> collectionResource;

    private C collection;

    private final int limit;

    private Iterator<T> iterator;

    private int counter;

    public PagedCollectionIterable(PagedCollectionResource<T, ? extends C> collectionResource, C collection) {
        this(collectionResource, collection, 0);
    }

    public PagedCollectionIterable(PagedCollectionResource<T, ? extends C> collectionResource, C collection, int limit) {
        this.collectionResource = collectionResource;
        this.collection = collection;
        this.limit = limit;
        this.iterator = collection.iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (reachedLimit()) {
            return false;
        }
        if (iterator.hasNext()) {
            return true;
        }
        if (collection.getNext() == null) {
            return false;
        }
        collection = collectionResource.getNextPage(collection);
        iterator = collection.iterator();
        return iterator.hasNext();
    }

    public boolean reachedLimit() {
        return limit > 0 && counter < limit;
    }

    @Override
    public T next() {
        counter++;
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove from PagedCollectionIterable!");
    }
}
