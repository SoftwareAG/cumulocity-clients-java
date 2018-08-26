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
    private final String oAuthAccessToken;
    private final String xsrfToken;

    /**
     * Creates a new Authentication filter
     *
     * @param username
     * @param password
     * @param oAuthAccessToken
     * @param xsrfToken
     */
    public CumulocityAuthenticationFilter(final String username, final String password, final String oAuthAccessToken, final String xsrfToken) {
        try {
            authentication = "Basic " + new String(Base64.encode(username + ":" + password), "ASCII");
            this.oAuthAccessToken = oAuthAccessToken;
            this.xsrfToken = xsrfToken;
        } catch (UnsupportedEncodingException ex) {
            // This should never occur
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

        if (oAuthAccessToken == null) {
            if (!cr.getMetadata().containsKey(HttpHeaders.AUTHORIZATION)) {
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, authentication);
            }
        } else {
            cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
            cr.getHeaders().putSingle("Cookie", "authorization=" + oAuthAccessToken);
            cr.getHeaders().putSingle("X-XSRF-TOKEN", xsrfToken);
        }
        
        return getNext().handle(cr);
    }

}
