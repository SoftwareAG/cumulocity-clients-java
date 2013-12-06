package com.cumulocity.sdk.client;

public interface PagedCollectionRepresentation<T> {

    Iterable<T> allPages();

    Iterable<T> elements(int limit);

}
