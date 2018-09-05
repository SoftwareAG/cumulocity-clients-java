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

import com.cumulocity.rest.mediatypes.ErrorMessageRepresentationReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyReader;
import com.cumulocity.rest.providers.CumulocityJSONMessageBodyWriter;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.ResourceRepresentation;
import com.cumulocity.rest.representation.ResourceRepresentationWithId;
import com.cumulocity.sdk.client.buffering.BufferRequestService;
import com.cumulocity.sdk.client.buffering.BufferedRequest;
import com.cumulocity.sdk.client.buffering.Future;
import com.cumulocity.sdk.client.interceptor.HttpClientInterceptor;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import static com.sun.jersey.api.client.ClientResponse.Status.*;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

public class RestConnector implements RestOperations {

    public static final class ProxyHttpURLConnectionFactory implements HttpURLConnectionFactory {

        Proxy proxy;

        public ProxyHttpURLConnectionFactory(PlatformParameters platformParameters) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(platformParameters.getProxyHost(), platformParameters.getProxyPort()));
        }

        public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection(proxy);
        }
    }

    public static final String X_CUMULOCITY_APPLICATION_KEY = "X-Cumulocity-Application-Key";
    
    public static final String X_CUMULOCITY_REQUEST_ORIGIN = "X-Cumulocity-Request-Origin";
    
    private static final String TFA_TOKEN_HEADER = "TFAToken";

    private final static Class<?>[] PROVIDERS_CLASSES = {CumulocityJSONMessageBodyWriter.class, CumulocityJSONMessageBodyReader.class,
        ErrorMessageRepresentationReader.class};

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

    @Override
    public <T extends ResourceRepresentation> T get(String path, CumulocityMediaType mediaType, Class<T> responseType) throws SDKException {
        ClientResponse response = getClientResponse(path, mediaType);
        return responseParser.parse(response, responseType, OK.getStatusCode());
    }
    
    @Override
    public <T extends Object> T get(String path, MediaType mediaType, Class<T> responseType) throws SDKException {
        ClientResponse response = getClientResponse(path, mediaType);
        return responseParser.parseObject(response, OK.getStatusCode(), responseType);
    }

    public ClientResponse get(String path, MediaType mediaType) {
        return getClientResponse(path, mediaType);
    }

    @Override
    public Response.Status getStatus(String path, CumulocityMediaType mediaType) throws SDKException {
        ClientResponse response = getClientResponse(path, mediaType);
        return response.getResponseStatus();
    }

    private ClientResponse getClientResponse(String path, MediaType mediaType) {
        Builder builder = client.resource(path).accept(mediaType);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        return builder.get(ClientResponse.class);
    }

    @Override
    public <T extends ResourceRepresentation> T postStream(String path, CumulocityMediaType mediaType, InputStream content,
                                                           Class<T> responseClass) throws SDKException {
        WebResource.Builder builder = client.resource(path).type(MULTIPART_FORM_DATA);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return parseResponseWithoutId(responseClass, builder.post(ClientResponse.class, form), CREATED.getStatusCode());

    }
    
    @Override
    public <T extends ResourceRepresentation> T postText(String path, String content, Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(MediaType.TEXT_PLAIN);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        return parseResponseWithoutId(responseClass, builder.post(ClientResponse.class, content), CREATED.getStatusCode());
    }

    @Override
    public <T extends ResourceRepresentation> T putText(String path, String content, Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(MediaType.TEXT_PLAIN);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        return parseResponseWithoutId(responseClass, builder.put(ClientResponse.class, content), OK.getStatusCode());
    }

    @Override
    public <T extends ResourceRepresentation> T putStream(String path, String contentType, InputStream content,
                                                          Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(contentType);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(MediaType.APPLICATION_JSON);
        }
        return parseResponseWithoutId(responseClass, builder.put(ClientResponse.class, content), CREATED.getStatusCode());
    }
    
    @Override
    public <T extends ResourceRepresentation> T putStream(String path, MediaType mediaType, InputStream content,
                                                          Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(MULTIPART_FORM_DATA);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("file", content, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return parseResponseWithoutId(responseClass, builder.put(ClientResponse.class, form), OK.getStatusCode());
    }
    
    @Override
    public <T extends ResourceRepresentation> T postFile(String path, T representation, byte[] bytes,
                                                         Class<T> responseClass) {
        WebResource.Builder builder = client.resource(path).type(MULTIPART_FORM_DATA);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        if (platformParameters.requireResponseBody()) {
            builder.accept(MediaType.APPLICATION_JSON);
        }
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FormDataBodyPart("object", representation, MediaType.APPLICATION_JSON_TYPE));
        form.bodyPart(new FormDataBodyPart("filesize", String.valueOf(bytes.length)));
        form.bodyPart(new FormDataBodyPart("file", bytes, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return parseResponseWithoutId(responseClass, builder.post(ClientResponse.class, form), CREATED.getStatusCode());
    }

    @Override
    public <T extends ResourceRepresentationWithId> T put(String path, MediaType mediaType, T representation) throws SDKException {
        ClientResponse response = httpPut(path, mediaType, representation);
        return parseResponseWithId(representation, response, OK.getStatusCode());
    }

    private <T extends ResourceRepresentationWithId> T parseResponseWithId(T representation, ClientResponse response, int responseCode)
            throws SDKException {
        @SuppressWarnings("unchecked")
        T repFromPlatform = responseParser.parse(response, (Class<T>) representation.getClass(), responseCode);
        T repToReturn = isDefined(repFromPlatform) ? repFromPlatform : representation;
        if (response.getLocation() != null) {
            repToReturn.setId(responseParser.parseIdFromLocation(response));
        }
        return repToReturn;
    }

    private <T extends ResourceRepresentationWithId> boolean isDefined(T repFromPlatform) {
        return repFromPlatform != null;
    }

    @Override
    public <T extends ResourceRepresentation> Future postAsync(String path, CumulocityMediaType mediaType, T representation)
            throws SDKException {
        return sendAsyncRequest(HttpMethod.POST, path, mediaType, representation);
    }

    @Override
    public <T extends ResourceRepresentation> Future putAsync(String path, CumulocityMediaType mediaType, T representation)
            throws SDKException {
        return sendAsyncRequest(HttpMethod.PUT, path, mediaType, representation);
    }

    private <T extends ResourceRepresentation> Future sendAsyncRequest(String method, String path, CumulocityMediaType mediaType,
            T representation) {
        BufferRequestService bufferRequestService = platformParameters.getBufferRequestService();
        return bufferRequestService.create(BufferedRequest.create(method, path, mediaType, representation));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourceRepresentation> T post(String path, MediaType mediaType, T representation) throws SDKException {
        ClientResponse response = httpPost(path, mediaType, mediaType, representation);
        return (T) parseResponseWithoutId(representation.getClass(), response, CREATED.getStatusCode(), OK.getStatusCode());
    }

    @Override
    public <T extends ResourceRepresentationWithId> T post(String path, MediaType mediaType, T representation) throws SDKException {
        ClientResponse response = httpPost(path, mediaType, mediaType, representation);
        return parseResponseWithId(representation, response, CREATED.getStatusCode());
    }

    @Override
    public <T extends ResourceRepresentation> void postWithoutResponse(String path, MediaType mediaType, T representation) throws SDKException {
        WebResource.Builder builder = client.resource(path).type(mediaType);
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        ClientResponse response = builder.post(ClientResponse.class, representation);
        responseParser.checkStatus(response, CREATED.getStatusCode(), ACCEPTED.getStatusCode());
    }

    @Override
    public <Result extends ResourceRepresentation, Param extends ResourceRepresentation> Result post(
            final String path,
            final CumulocityMediaType contentType,
            final CumulocityMediaType accept,
            final Param representation,
            final Class<Result> clazz) {
        ClientResponse response = httpPost(path, contentType, accept, representation);
        return parseResponseWithoutId(clazz, response, Response.Status.OK.getStatusCode());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourceRepresentation> T put(String path, MediaType mediaType, T representation) throws SDKException {

        ClientResponse response = httpPut(path, mediaType, representation);
        return (T) parseResponseWithoutId(representation.getClass(), response, OK.getStatusCode());
    }

    private Builder addApplicationKeyHeader(Builder builder) {
        if (platformParameters.getApplicationKey() != null) {
            builder = builder.header(X_CUMULOCITY_APPLICATION_KEY, platformParameters.getApplicationKey());
        }
        return builder;
    }
    
    private Builder addTfaHeader(Builder builder) {
        if (platformParameters.getTfaToken() != null) {
            builder = builder.header(TFA_TOKEN_HEADER, platformParameters.getTfaToken());
        }
        return builder;
    }
    
    private Builder addRequestOriginHeader(Builder builder) {
        if (platformParameters.getRequestOrigin() != null) {
            builder = builder.header(X_CUMULOCITY_REQUEST_ORIGIN, platformParameters.getRequestOrigin());
        }
        return builder;
    }

    private Builder applyInterceptors(Builder builder) {
        if (platformParameters.interceptorSet != null) {
            synchronized (platformParameters.interceptorSet) {
                for (HttpClientInterceptor interceptor : platformParameters.interceptorSet) {
                    builder = interceptor.apply(builder);
                }
            }
        }
        return builder;
    }

    private <T extends ResourceRepresentation> T parseResponseWithoutId(Class<T> type, ClientResponse response, int... responseCodes)
            throws SDKException {
        return responseParser.parse(response, type, responseCodes);
    }

    private <T extends ResourceRepresentation> ClientResponse httpPost(String path, MediaType contentType, MediaType accept, T representation) {
        WebResource.Builder builder = client.resource(path).type(contentType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(accept);
        }
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        return builder.post(ClientResponse.class, responseParser.write(representation));
    }

    private <T extends ResourceRepresentation> ClientResponse httpPut(String path, MediaType mediaType, T representation) {

        WebResource.Builder builder = client.resource(path).type(mediaType);
        if (platformParameters.requireResponseBody()) {
            builder.accept(mediaType);
        }
        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        return builder.put(ClientResponse.class, responseParser.write(representation));
    }

    @Override
    public void delete(String path) throws SDKException {
        Builder builder = client.resource(path).getRequestBuilder();

        builder = addApplicationKeyHeader(builder);
        builder = addTfaHeader(builder);
        builder = addRequestOriginHeader(builder);
        builder = applyInterceptors(builder);
        ClientResponse response = builder.delete(ClientResponse.class);
        responseParser.checkStatus(response, NO_CONTENT.getStatusCode());
    }

    public static Client createClient(PlatformParameters platformParameters) {

        DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();

        if (isProxyRequired(platformParameters)) {
            config.getProperties().put(ApacheHttpClientConfig.PROPERTY_PROXY_URI,
                    "http://" + platformParameters.getProxyHost() + ":" + platformParameters.getProxyPort());
            if (isProxyAuthenticationRequired(platformParameters)) {
                config.getState().setProxyCredentials(null, platformParameters.getProxyHost(), platformParameters.getProxyPort(),
                        platformParameters.getProxyUserId(), platformParameters.getProxyPassword());
            }
        }

        registerClasses(config);
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_READ_TIMEOUT, READ_TIMEOUT_IN_MILLIS);

        CumulocityHttpClient client = new CumulocityHttpClient(createDefaultClientHander(config), null);
        client.setPlatformParameters(platformParameters);
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter(platformParameters.getPrincipal(), platformParameters.getPassword()));
        return client;
    }

    private static ApacheHttpClientHandler createDefaultClientHander(ClientConfig cc) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.setMaxConnectionsPerHost(20);
        final HttpClient client = new HttpClient(httpConnectionManager);
        client.getParams().setConnectionManagerTimeout(10000);
        return new ApacheHttpClientHandler(client, cc);
    }

    private static boolean isProxyAuthenticationRequired(PlatformParameters platformParameters) {
        return hasText(platformParameters.getProxyUserId()) && hasText(platformParameters.getProxyPassword());
    }

    private static boolean isProxyRequired(PlatformParameters platformParameters) {
        return hasText(platformParameters.getProxyHost()) && (platformParameters.getProxyPort() > 0);
    }

    public static Client createURLConnectionClient(final PlatformParameters platformParameters) {

        ClientConfig config = new DefaultClientConfig();

        registerClasses(config);

        Client client = new Client(new URLConnectionClientHandler(resolveConnectionFactory(platformParameters)), config);
        client.setReadTimeout(READ_TIMEOUT_IN_MILLIS);
        client.setFollowRedirects(true);
        client.addFilter(new HTTPBasicAuthFilter(platformParameters.getPrincipal(), platformParameters.getPassword()));
        if (isProxyRequired(platformParameters) && isProxyAuthenticationRequired(platformParameters)) {
            client.addFilter(new HTTPBasicProxyAuthenticationFilter(platformParameters.getProxyUserId(), platformParameters
                    .getProxyPassword()));
        }
        return client;
    }

    private static HttpURLConnectionFactory resolveConnectionFactory(final PlatformParameters platformParameters) {
        return isProxyRequired(platformParameters) ? new ProxyHttpURLConnectionFactory(platformParameters) : null;
    }

    private static void registerClasses(ClientConfig config) {
        for (Class<?> c : PROVIDERS_CLASSES) {
            config.getClasses().add(c);
        }
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
