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

import static com.sun.jersey.api.client.ClientResponse.Status.CREATED;
import static com.sun.jersey.api.client.ClientResponse.Status.NO_CONTENT;
import static com.sun.jersey.api.client.ClientResponse.Status.OK;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

import java.io.InputStream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import com.cumulocity.rest.mediatypes.ErrorMessageRepresentationReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyWriter;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import com.cumulocity.sdk.client.buffering.BufferRequestService;
import com.cumulocity.sdk.client.buffering.BufferedRequest;
import com.cumulocity.sdk.client.buffering.Future;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class RestConnector {
    
    public static final String X_CUMULOCITY_APPLICATION_KEY = "X-Cumulocity-Application-Key";
    
    private final static Class<?>[] PROVIDERS_CLASSES = {
        CumulocityJSONMessageBodyWriter.class,
        CumulocityJSONMessageBodyReader.class,
        ErrorMessageRepresentationReader.class
    };

    private static final int READ_TIMEOUT_IN_MILLIS = 180000;
    
    private final PlatformParameters platformParameters;
    
    private final Client client;
    
    private final ResponseParser responseParser;

    public RestConnector(PlatformParameters platformParameters, ResponseParser responseParser) {
        this(platformParameters, responseParser, createClient(platformParameters));
    }
    
    protected RestConnector(PlatformParameters platformParameters, ResponseParser responseParser, Client client) {
        this.platformParameters = platformParameters;
        this.responseParser = responseParser;
        this.client = client;
    }

    public PlatformParameters getPlatformParameters() {
        return platformParameters;
    }

    public Client getClient() {
        return client;
    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }

    public <T extends ResourceRepresentation> T get(String path, CumulocityMediaType mediaType,
            Class<T> responseType) throws SDKException {
         Builder builder = client.resource(path).accept(mediaType);
         builder = addApplicationKeyHeader(builder);
         ClientResponse response = builder.get(ClientResponse.class);
        return responseParser.parse(response, OK.getStatusCode(), responseType);
    }

    public <T extends ResourceRepresentation> T postStream(String path, CumulocityMediaType mediaType, InputStream content,
            Class<T> responseClass) throws SDKException {
        WebResource.Builder builder = client.resource(path).type(MULTIPART_FORM_DATA);
        builder = addApplicationKeyHeader(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return parseResponseWithoutId(responseClass, builder.post(ClientResponse.class, form), CREATED.getStatusCode());

    }

    public <T extends ResourceRepresentation> T putStream(String path, CumulocityMediaType mediaType, InputStream content,
            Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(MULTIPART_FORM_DATA);
        builder = addApplicationKeyHeader(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return parseResponseWithoutId(responseClass, builder.put(ClientResponse.class, form), OK.getStatusCode());
    }

    public <T extends ResourceRepresentationWithId> T put(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {

        ClientResponse response = httpPut(path, mediaType, representation);
        return parseResponseWithId(representation, response, OK.getStatusCode());

    } 

    private <T extends ResourceRepresentationWithId> T parseResponseWithId(T representation, ClientResponse response,
            int responseCode) throws SDKException {
        @SuppressWarnings("unchecked")
        T repFromPlatform = responseParser.parse(response, responseCode, (Class<T>) representation.getClass());
        T repToReturn = isDefined(repFromPlatform) ? repFromPlatform : representation;
        if (response.getLocation() != null) {
            repToReturn.setId(responseParser.parseIdFromLocation(response));
        }
        return repToReturn;
    }

    private <T extends ResourceRepresentationWithId> boolean isDefined(T repFromPlatform) {
        return repFromPlatform != null;
    }
    
    public <T extends ResourceRepresentation> Future postAsync(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {
        return sendAsyncRequest(HttpMethod.POST, path, mediaType, representation);
    }
    
    public <T extends ResourceRepresentation> Future putAsync(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {
        return sendAsyncRequest(HttpMethod.PUT, path, mediaType, representation);
    }

    private <T extends ResourceRepresentation> Future sendAsyncRequest(String method, String path, CumulocityMediaType mediaType, T representation) {
        BufferRequestService bufferRequestService = platformParameters.getBufferRequestService();
        return bufferRequestService.create(BufferedRequest.create(method, path, mediaType, representation));
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourceRepresentation> T post(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {
        ClientResponse response = httpPost(path, mediaType, representation);
        return (T) parseResponseWithoutId(representation.getClass(), response, CREATED.getStatusCode());
    }
    
    public <T extends ResourceRepresentationWithId> T post(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {
        ClientResponse response = httpPost(path, mediaType, representation);
        return parseResponseWithId(representation, response, CREATED.getStatusCode());
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourceRepresentation> T put(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {

        ClientResponse response = httpPut(path, mediaType, representation);
        return (T) parseResponseWithoutId(representation.getClass(), response, OK.getStatusCode());
    }
    
    private Builder addApplicationKeyHeader(Builder builder) {
        if (platformParameters.getApplicationKey() != null) {
             builder = builder.header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey());
         }
        return builder;
    }

    private <T extends ResourceRepresentation> T parseResponseWithoutId(Class<T> type, ClientResponse response,
            int responseCode) throws SDKException {
        return responseParser.parse(response, responseCode, type);
    }

    private <T extends ResourceRepresentation> ClientResponse httpPost(String path,
            CumulocityMediaType mediaType, T representation) {
        WebResource.Builder builder = client.resource(path).type(mediaType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        builder = addApplicationKeyHeader(builder);
        return builder.post(ClientResponse.class,
                representation);
    }
    
    private <T extends ResourceRepresentation> ClientResponse httpPut(String path, CumulocityMediaType mediaType,
            T representation) {
        
        WebResource.Builder builder = client.resource(path).type(mediaType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        builder = addApplicationKeyHeader(builder);
        return builder.put(ClientResponse.class, representation);
    }

    public void delete(String path) throws SDKException {
        Builder builder = client.resource(path).getRequestBuilder();
        
        builder = addApplicationKeyHeader(builder);
        ClientResponse response = builder.delete(ClientResponse.class);
        responseParser.checkStatus(response, NO_CONTENT.getStatusCode());
    }

    public static Client createClient(PlatformParameters platformParameters) {

        DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();

        if (hasText(platformParameters.getProxyHost()) && (platformParameters.getProxyPort() > 0)) {
            config.getProperties().put(ApacheHttpClientConfig.PROPERTY_PROXY_URI,
                    "http://" + platformParameters.getProxyHost() + ":" + platformParameters.getProxyPort());
            if (hasText(platformParameters.getProxyUserId()) && hasText(platformParameters.getProxyPassword())) {
                config.getState().setProxyCredentials(null, platformParameters.getProxyHost(),
                        platformParameters.getProxyPort(), platformParameters.getProxyUserId(),
                        platformParameters.getProxyPassword());
            }
        }

        for (Class<?> c : PROVIDERS_CLASSES) {
            config.getClasses().add(c);
        }
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_READ_TIMEOUT, READ_TIMEOUT_IN_MILLIS);

        Client client = ApacheHttpClient.create(config);
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter(platformParameters.getPrincipal(), platformParameters.getPassword()));
        
        return client;
    }

    private static boolean hasText(String string) {
        if (string == null) {
            return false;
        }
        if (string.trim().length() <= 0) {
            return false;
        }
        return true;
    }

}
