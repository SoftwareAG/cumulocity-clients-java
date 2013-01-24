package com.cumulocity.sdk.client;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;

public class EmptyPagedCollectionResourceTest {

    private EmptyPagedCollectionResource<ManagedObjectCollectionRepresentation> emptyCollectionResource = new EmptyPagedCollectionResource<ManagedObjectCollectionRepresentation>(
            ManagedObjectCollectionRepresentation.class);

    @Test
    public void getShouldReturnEmptyList() throws Exception {
        // When
        ManagedObjectCollectionRepresentation representation = emptyCollectionResource.get();
        // Then
        assertThatContainsEmptyList(representation);
    }

    @Test
    public void getPageShouldReturnEmptyListForFirstPage() throws Exception {
        // When
        ManagedObjectCollectionRepresentation representation = emptyCollectionResource.getPage(null, 1);
        // Then
        assertThatContainsEmptyList(representation);
    }

    @Test
    public void getNextPageShouldReturnNull() throws Exception {
        // When
        ManagedObjectCollectionRepresentation representation = emptyCollectionResource.getNextPage(null);
        // Then
        assertThat(representation, is(nullValue()));
    }

    @Test
    public void getPreviousPageShouldReturnNull() throws Exception {
        // When
        ManagedObjectCollectionRepresentation representation = emptyCollectionResource.getPreviousPage(null);
        // Then
        assertThat(representation, is(nullValue()));
    }

    private void assertThatContainsEmptyList(ManagedObjectCollectionRepresentation representation) {
        assertThat(representation, is(notNullValue()));
        assertThat(representation.getManagedObjects().size(), is(equalTo(0)));
    }
}
