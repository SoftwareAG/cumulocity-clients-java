package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import javax.ws.rs.core.HttpHeaders;
import java.io.UnsupportedEncodingException;

public class CumulocityAuthenticationFilter extends ClientFilter {

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

    public CumulocityAuthenticationFilter(final String oAuthAccessToken, final String xsrfToken) {
        this.oAuthAccessToken = oAuthAccessToken;
        this.xsrfToken = xsrfToken;
        this.authentication = null;
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

        if (oAuthAccessToken == null) {
            if (!cr.getMetadata().containsKey(HttpHeaders.AUTHORIZATION)) {
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, authentication);
            }
        } else {
            /** There is an implicit assumption at this point, that if we reach this point and there is access token
             without xsrf token, then it was authorized with Bearer token, but if there was X-XSRF-TOKEN, the request was
             authorized with cookies and cookie authorization must be in place for to server request
             */
            if (xsrfToken == null) {
            cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthAccessToken);
            } else {
                cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
                cr.getHeaders().putSingle("Cookie", "authorization=" + oAuthAccessToken);
                cr.getHeaders().putSingle("X-XSRF-TOKEN", xsrfToken);
            }
        }
        
        return getNext().handle(cr);
    }

}
