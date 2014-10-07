package com.cumulocity.sdk.client;

import java.io.UnsupportedEncodingException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

class HTTPBasicProxyAuthenticationFilter extends ClientFilter {

    private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

    private final String authentication;

    /**
     * Creates a new HTTP Proxy-Authorization filter using provided username
     * and password credentials
     *
     * @param username
     * @param password
     */
    public HTTPBasicProxyAuthenticationFilter(final String username, final String password) {
        try {
            authentication = "Basic " + new String(Base64.encode(username + ":" + password), "ASCII");
        } catch (UnsupportedEncodingException ex) {
            // This should never occur
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

        if (!cr.getHeaders().containsKey(PROXY_AUTHORIZATION)) {
            cr.getHeaders().putSingle(PROXY_AUTHORIZATION, authentication);
        }
        return getNext().handle(cr);
    }

}
