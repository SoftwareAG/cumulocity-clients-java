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

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.BaseResourceRepresentation;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.interceptor.HttpClientInterceptor;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RestConnectorTest {

    private static final String PATH = "path";

    private CumulocityMediaType mediaType;

    @Mock
    private WebResource webResource;

    @Mock
    private ClientResponse response;

    @Mock
    private Builder typeBuilder;

    private PlatformParameters clientParameters = new PlatformParameters("http://host", CumulocityBasicCredentials.builder().build(), null);

    @Mock
    private Client client;

    @Mock
    private ResponseParser parser;

    private RestConnector restConnector;

    @Before
    public void initializingMediaTypeAtDeclarationCausesPowerMockAndJerseyToThrowALinkageError() {
        mediaType = CumulocityMediaType.ERROR_MESSAGE;
    }

    @Before
    public void setup() {
        restConnector = new RestConnector(clientParameters, parser, client);

        when(client.resource(PATH)).thenReturn(webResource);

        when(parser.write(anyObject())).thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    @Test
    public void shouldRetrieveResource() throws Exception {
        // Given
        when(webResource.accept(mediaType)).thenReturn(typeBuilder);
        when(typeBuilder.get(ClientResponse.class)).thenReturn(response);

        BaseResourceRepresentation representation = new BaseResourceRepresentation();
        when(parser.parse(response, BaseResourceRepresentation.class, 200)).thenReturn(representation);

        // When
        BaseResourceRepresentation result = restConnector.get(PATH, mediaType,
                BaseResourceRepresentation.class);

        // Then
        assertThat(result, sameInstance(representation));
    }

    @Test
    public void shouldPostBaseRepresentation() throws Exception {
        // Given
        BaseResourceRepresentation representation = new BaseResourceRepresentation();
        returnResponseWhenPosting(representation);
        BaseResourceRepresentation outputRepresentation = new BaseResourceRepresentation();
        when(parser.parse(response, BaseResourceRepresentation.class, 201, 200)).thenReturn(outputRepresentation);

        // When
        BaseResourceRepresentation result = restConnector.post(PATH, mediaType, representation);

        // Then
        assertThat(result, sameInstance(outputRepresentation));
    }

    @Test
    public void shouldPostRepresentationSupportingId() throws Exception {
        // Given
        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPosting(representation);
        ManagedObjectRepresentation outputRepresentation = new ManagedObjectRepresentation();
        when(parser.parse(response, ManagedObjectRepresentation.class, 201)).thenReturn(outputRepresentation);

        // When
        ManagedObjectRepresentation result = restConnector.post(PATH, mediaType, representation);

        // Then
        assertThat(result, sameInstance(outputRepresentation));
    }

    @Test
    public void shouldPostRepresentationSupportingIdWithNoResponseBody() throws Exception {
        // Given
        clientParameters.setRequireResponseBody(false);

        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPosting(representation);
        when(parser.parse(response, ManagedObjectRepresentation.class, 201)).thenReturn(null);
        when(response.getLocation()).thenReturn(new URI("http://URI"));
        when(parser.parseIdFromLocation(response)).thenReturn(new GId("mo_id"));

        // When
        ManagedObjectRepresentation result = restConnector.post(PATH, mediaType, representation);

        // Then
        verify(typeBuilder, never()).accept(any(MediaType.class));
        assertThat(result, sameInstance(representation));
        assertThat(result.getId(), is(new GId("mo_id")));
    }

    @Test
    public void shouldPostRepresentationSupportingIdWithNoResponseBodyAndNoLocation() throws Exception {
        // Given
        clientParameters.setRequireResponseBody(false);

        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPosting(representation);
        when(parser.parse(response, ManagedObjectRepresentation.class, 201)).thenReturn(null);
        when(response.getLocation()).thenReturn(null);

        // When
        ManagedObjectRepresentation result = restConnector.post(PATH, mediaType, representation);

        // Then
        verify(typeBuilder, never()).accept(any(MediaType.class));
        assertThat(result, sameInstance(representation));
        assertThat(result.getId(), nullValue());
    }


    private void returnResponseWhenPosting(BaseResourceRepresentation representation) {
        returnResponseWhenSending();
        when(typeBuilder.post(ClientResponse.class, representation)).thenReturn(response);
    }

    private void returnResponseWhenSending() {
        when(webResource.type(mediaType)).thenReturn(typeBuilder);
        when(typeBuilder.accept(mediaType)).thenReturn(typeBuilder);
    }

    @Test
    public void shouldDelete() throws Exception {
        // Given
        when(webResource.getRequestBuilder()).thenReturn(typeBuilder);
        when(typeBuilder.delete(ClientResponse.class)).thenReturn(response);

        // When
        restConnector.delete(PATH);

        // Then
        verify(parser).checkStatus(response, 204);
    }

    @Test
    public void shouldPutBaseRepresentation() throws Exception {
        // Given
        BaseResourceRepresentation representation = new BaseResourceRepresentation();
        BaseResourceRepresentation outputRepresentation = new BaseResourceRepresentation();
        returnResponseWhenPut(representation);
        when(parser.parse(response, BaseResourceRepresentation.class, 200)).thenReturn(outputRepresentation);

        // When
        BaseResourceRepresentation result = restConnector.put(PATH, mediaType, representation);

        // Then
        assertThat(result, sameInstance(outputRepresentation));
    }

    private void returnResponseWhenPut(BaseResourceRepresentation representation) {
        returnResponseWhenSending();
        when(typeBuilder.put(ClientResponse.class, representation)).thenReturn(response);
    }

    @Test
    public void shouldPutRepresentationSupportingId() throws Exception {
        // Given
        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPut(representation);
        ManagedObjectRepresentation outputRepresentation = new ManagedObjectRepresentation();
        when(parser.parse(response, ManagedObjectRepresentation.class, 200)).thenReturn(outputRepresentation);

        // When
        ManagedObjectRepresentation result = restConnector.put(PATH, mediaType, representation);

        // Then
        assertThat(result, sameInstance(outputRepresentation));
    }

    @Test
    public void shouldPutRepresentationSupportingIdWithNoResponseBody() throws Exception {
        // Given
        clientParameters.setRequireResponseBody(false);

        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPut(representation);
        when(parser.parse(response, ManagedObjectRepresentation.class, 200)).thenReturn(null);
        when(response.getLocation()).thenReturn(new URI("http://URI"));
        when(parser.parseIdFromLocation(response)).thenReturn(new GId("mo_id"));

        // When
        ManagedObjectRepresentation result = restConnector.put(PATH, mediaType, representation);

        // Then
        verify(typeBuilder, never()).accept(any(MediaType.class));
        assertThat(result, sameInstance(representation));
        assertThat(result.getId(), is(new GId("mo_id")));
    }

    @Test
    public void shouldPutRepresentationSupportingIdWithNoResponseBodyAndNoLocation() throws Exception {
        // Given
        clientParameters.setRequireResponseBody(false);

        ManagedObjectRepresentation representation = new ManagedObjectRepresentation();
        returnResponseWhenPut(representation);
        when(parser.parse(response, ManagedObjectRepresentation.class, 200)).thenReturn(null);
        when(response.getLocation()).thenReturn(null);

        // When
        ManagedObjectRepresentation result = restConnector.put(PATH, mediaType, representation);

        // Then
        verify(typeBuilder, never()).accept(any(MediaType.class));
        assertThat(result, sameInstance(representation));
        assertThat(result.getId(), nullValue());
    }

    @Test
    public void shouldAddHeaderFormInterceptor() throws Exception {
        // Given
        clientParameters.registerInterceptor(new HttpClientInterceptor() {
            @Override
            public Builder apply(Builder builder) {
                builder.header("fake", "fake");
                return builder;
            }
        });

        BaseResourceRepresentation representation = new BaseResourceRepresentation();
        returnResponseWhenPosting(representation);
        BaseResourceRepresentation outputRepresentation = new BaseResourceRepresentation();
        when(parser.parse(response, BaseResourceRepresentation.class, 201, 200)).thenReturn(outputRepresentation);

        // When
        BaseResourceRepresentation result = restConnector.post(PATH, mediaType, representation);

        // Then
        verify(typeBuilder).header(eq("fake"), eq("fake"));
        assertThat(result, sameInstance(outputRepresentation));
    }

    @Test
    public void shouldNotAddHeaderFormInterceptorAfterRemovingInterceptor() throws Exception {
        // given
        HttpClientInterceptor addHeaderFake = new HttpClientInterceptor() {
            @Override
            public Builder apply(Builder builder) {
                builder.header("fake", "fake");
                return builder;
            }
        };

        BaseResourceRepresentation representation = new BaseResourceRepresentation();
        returnResponseWhenPosting(representation);
        BaseResourceRepresentation outputRepresentation = new BaseResourceRepresentation();
        when(parser.parse(response, BaseResourceRepresentation.class, 201)).thenReturn(outputRepresentation);

        { // recheck if work ok on register interceptor
            clientParameters.registerInterceptor(addHeaderFake);
            restConnector.post(PATH, mediaType, representation);
            verify(typeBuilder).header(eq("fake"), eq("fake"));
            reset(typeBuilder);
        }

        clientParameters.unregisterInterceptor(addHeaderFake);

        // when
        restConnector.post(PATH, mediaType, representation);

        // then
        verify(typeBuilder, never()).header(eq("fake"), eq("fake"));
    }

}
