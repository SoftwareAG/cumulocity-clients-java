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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.rest.representation.ErrorMessageRepresentation;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.sun.jersey.api.client.ClientResponse;

public class ResponseParserTest {

    private static final int EXPECTED_STATUS = 200;

    private static final int ERROR_STATUS = 500;

    @Mock
    private ClientResponse response;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ResponseParser parser;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        parser = new ResponseParser();
    }

    @Test
    public void shouldParse() throws Exception {
        // Given
        when(response.getStatus()).thenReturn(EXPECTED_STATUS);
        BaseCumulocityResourceRepresentation representation = new BaseCumulocityResourceRepresentation();
        when(response.getEntity(BaseCumulocityResourceRepresentation.class)).thenReturn(representation);

        // When
        BaseCumulocityResourceRepresentation result = parser.parse(response, EXPECTED_STATUS,
                BaseCumulocityResourceRepresentation.class);

        // Then
        assertThat(result, sameInstance(representation));
    }

    @Test
    public void shouldParseDifferentSpecificType() throws Exception {
        // Given
        when(response.getStatus()).thenReturn(EXPECTED_STATUS);
        InventoryRepresentation representation = new InventoryRepresentation();
        when(response.getEntity(InventoryRepresentation.class)).thenReturn(representation);

        // When
        InventoryRepresentation result = parser.parse(response, EXPECTED_STATUS, InventoryRepresentation.class);

        // Then
        assertThat(result, sameInstance(representation));
    }

    @Test
    public void shouldIncludeExistingErrorMessageInExceptionWhenStatusIsNotAsExpected() throws Exception {
        // Given
        when(response.getStatus()).thenReturn(ERROR_STATUS);
        ErrorMessageRepresentation errorRepresentation = new ErrorMessageRepresentation();
        when(response.hasEntity()).thenReturn(true);
        when(response.getEntity(ErrorMessageRepresentation.class)).thenReturn(errorRepresentation);

        // Then
        thrown.expect(SDKException.class);
        thrown.expectMessage("Http status code: " + ERROR_STATUS + "\n" + errorRepresentation.toString());

        // When
        parser.parse(response, EXPECTED_STATUS, BaseCumulocityResourceRepresentation.class);
    }

    @Test
    public void shouldNotIncludeErrorMessageIfStatusIsNotAsExpected() throws Exception {
        // Given
        when(response.getStatus()).thenReturn(ERROR_STATUS);
        when(response.hasEntity()).thenReturn(false);
        when(response.getEntity(ErrorMessageRepresentation.class)).thenThrow(new RuntimeException());

        // Then
        thrown.expect(SDKException.class);
        thrown.expectMessage("Http status code: " + ERROR_STATUS);

        // When
        parser.parse(response, EXPECTED_STATUS, BaseCumulocityResourceRepresentation.class);
    }

    @Test
    public void shouldParseIdFromHeader() throws Exception {
        // Given
        when(response.getLocation()).thenReturn(
                new URI("http://integration.cumulocity.com/inventory/managedObjects/10200"));

        // When
        GId gid = parser.parseIdFromLocation(response);

        // Then
        assertThat(gid, is(new GId("10200")));
    }
}
