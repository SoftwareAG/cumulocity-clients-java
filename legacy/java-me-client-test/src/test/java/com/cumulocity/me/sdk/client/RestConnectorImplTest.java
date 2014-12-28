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
package com.cumulocity.me.sdk.client;

import static com.cumulocity.me.rest.representation.BaseCumulocityMediaType.ERROR_MESSAGE;
import static com.cumulocity.me.test.ReturnsThis.doReturnsThis;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Test;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.ResourceRepresentation;
import com.cumulocity.me.rest.representation.ErrorMessageRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnectorImpl;

public class RestConnectorImplTest {

    PlatformParameters platformParameters = new PlatformParameters("localhost", "test", "user", "pass", "appKey");
    WebClient httpClient = mock(WebClient.class);
    
    RestConnectorImpl restConnector = new RestConnectorImpl(platformParameters, httpClient);

    String path = "http://localhost/test/";
    CumulocityMediaType mediaType = ERROR_MESSAGE;
    Class<?> responseEntityType = ErrorMessageRepresentation.class;
    
    WebRequestBuilder requestBuilder = mock(WebRequestBuilder.class, doReturnsThis());
    
    @Test
    public void shouldGet() throws Exception {
        when(httpClient.request(path)).thenReturn(requestBuilder);
        when(requestBuilder.get(HttpConnection.HTTP_OK, responseEntityType)).thenReturn(new ErrorMessageRepresentation());
        
        ResourceRepresentation representation = restConnector.get(path, mediaType, responseEntityType);
        
        assertThat(representation).isInstanceOf(ErrorMessageRepresentation.class);
    }
}
