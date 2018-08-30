package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.base.Supplier;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import javax.ws.rs.core.HttpHeaders;
import java.io.UnsupportedEncodingException;

class CumulocityAuthenticationFilter extends ClientFilter {

    private final String authentication;
    private final Supplier<String> oAuthAccessToken;
    private final Supplier<String> xsrfToken;

    /**
     * Creates a new Authentication filter
     *
     * @param username
     * @param password
     * @param oAuthAccessToken
     * @param xsrfToken
     */
    public CumulocityAuthenticationFilter(final String username, final String password, final Supplier<String> oAuthAccessToken, final Supplier<String> xsrfToken) {
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

        if (oAuthAccessToken == null || oAuthAccessToken.get() == null) {
            if (!cr.getMetadata().containsKey(HttpHeaders.AUTHORIZATION)) {
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, authentication);
            }
        } else {
            cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
            cr.getHeaders().putSingle("Cookie", "authorization=" + oAuthAccessToken.get());
            cr.getHeaders().putSingle("X-XSRF-TOKEN", xsrfToken.get());
        }

        return getNext().handle(cr);
    }

}
