package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import javax.ws.rs.core.HttpHeaders;
import java.io.UnsupportedEncodingException;

class CumulocityAuthenticationFilter extends ClientFilter {

    private final String authentication;
    private final String token;

    /**
     * Creates a new Authentication filter
     *
     * @param username
     * @param password
     * @param token
     */
    public CumulocityAuthenticationFilter(final String username, final String password, final String token) {
        try {
            authentication = "Basic " + new String(Base64.encode(username + ":" + password), "ASCII");
            this.token = token;
        } catch (UnsupportedEncodingException ex) {
            // This should never occur
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

        if (!cr.getMetadata().containsKey(HttpHeaders.AUTHORIZATION) && token == null) {
            cr.getMetadata().add(HttpHeaders.AUTHORIZATION, authentication);
        } else {
            cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
            cr.getHeaders().putSingle("Cookie", "authorization=" + token);
        }

        return getNext().handle(cr);
    }

}
