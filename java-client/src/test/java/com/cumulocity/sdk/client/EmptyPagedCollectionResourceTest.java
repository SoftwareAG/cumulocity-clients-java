/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
