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

import com.cumulocity.rest.mediatypes.ErrorMessageRepresentationReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyWriter;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class RestConnector {
    
    public static final String X_CUMULOCITY_APPLICATION_KEY = "X-Cumulocity-Application-Key";
    
    private final static Class<?>[] PROVIDERS_CLASSES = {
        CumulocityJSONMessageBodyWriter.class,
        CumulocityJSONMessageBodyReader.class,
        ErrorMessageRepresentationReader.class
    };
    
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

    public <T extends ResourceRepresentation> T get(String path, CumulocityMediaType mediaType,
            Class<T> responseType) throws SDKException {
        ClientResponse response = client.resource(path).accept(mediaType)
                .header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey()).get(ClientResponse.class);
        return responseParser.parse(response, OK.getStatusCode(), responseType);
    }

    public <T extends ResourceRepresentationWithId> T post(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {

        ClientResponse response = httpPost(path, mediaType, representation);
        return parseResponseWithId(representation, response, CREATED.getStatusCode());

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

    public <T extends ResourceRepresentation> T post(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {

        ClientResponse response = httpPost(path, mediaType, representation);
        return parseResponseWithoutId(representation, response, CREATED.getStatusCode());
    }

    public <T extends ResourceRepresentation> T put(String path, CumulocityMediaType mediaType,
            T representation) throws SDKException {

        ClientResponse response = httpPut(path, mediaType, representation);
        return parseResponseWithoutId(representation, response, OK.getStatusCode());
    }

    private <T extends ResourceRepresentation> T parseResponseWithoutId(T representation, ClientResponse response,
            int responseCode) throws SDKException {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) representation.getClass();
        return responseParser.parse(response, responseCode, type);
    }

    private <T extends ResourceRepresentation> ClientResponse httpPost(String path,
            CumulocityMediaType mediaType, T representation) {
        
        WebResource.Builder builder = client.resource(path).type(mediaType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        return builder.header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey()).post(ClientResponse.class,
                representation);
    }

    private <T extends ResourceRepresentation> ClientResponse httpPut(String path, CumulocityMediaType mediaType,
            T representation) {
        
        WebResource.Builder builder = client.resource(path).type(mediaType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        return builder.header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey()).put(
                ClientResponse.class, representation);
    }

    public void delete(String path) throws SDKException {
        ClientResponse response = client.resource(path).header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey())
                .delete(ClientResponse.class);
        responseParser.checkStatus(response, NO_CONTENT.getStatusCode());
    }

    private static Client createClient(PlatformParameters platformParameters) {

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

        Client client = ApacheHttpClient.create(config);
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter(platformParameters.getTenantId() + "/" + platformParameters.getUser(),
                platformParameters.getPassword()));
        
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
