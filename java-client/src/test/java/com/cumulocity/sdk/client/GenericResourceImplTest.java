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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.rest.representation.CumulocityMediaType;

public class GenericResourceImplTest {

    private static final Class<BaseCumulocityResourceRepresentation> RESPONSE_TYPE = BaseCumulocityResourceRepresentation.class;

    private static final String URL = "path_to_resource";

    private static final CumulocityMediaType MEDIA_TYPE = new CumulocityMediaType();

    @Mock
    private RestConnector restConnector;

    private GenericResourceImpl<BaseCumulocityResourceRepresentation> resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        resource = new GenericResourceImpl<BaseCumulocityResourceRepresentation>(restConnector, URL) {

            @Override
            protected CumulocityMediaType getMediaType() {
                return MEDIA_TYPE;
            }

            @Override
            protected Class<BaseCumulocityResourceRepresentation> getResponseClass() {
                return RESPONSE_TYPE;
            }

        };
    }

    @Test
    public void shouldUseRestConnector() throws Exception {
        // given
        BaseCumulocityResourceRepresentation representation = new BaseCumulocityResourceRepresentation();
        when(restConnector.get(URL, MEDIA_TYPE, RESPONSE_TYPE)).thenReturn(representation);

        // when
        BaseCumulocityResourceRepresentation result = resource.get();

        // then
        assertThat(result, sameInstance(representation));

    }

}
