package com.cumulocity.sdk.client;

import com.cumulocity.sdk.client.base.Supplier;
import com.cumulocity.sdk.client.base.Suppliers;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.HttpHeaders;
import java.io.UnsupportedEncodingException;

@Slf4j
public class CumulocityAuthenticationFilter extends ClientFilter {

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

    public CumulocityAuthenticationFilter(final String oAuthAccessToken, final String xsrfToken) {
        this.oAuthAccessToken = Suppliers.ofInstance(oAuthAccessToken);
        this.xsrfToken = Suppliers.ofInstance(xsrfToken);
        this.authentication = null;
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

        if (unwrapSupplier(oAuthAccessToken) == null) {
            if (!cr.getMetadata().containsKey(HttpHeaders.AUTHORIZATION)) {
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, authentication);
            }
        } else {
            /** There is an implicit assumption at this point, that if we reach this point and there is access token
             without xsrf token, then it was authorized with Bearer token, but if there was X-XSRF-TOKEN, the request was
             authorized with cookies and cookie authorization must be in place for to server request
             */
            if (unwrapSupplier(xsrfToken) == null) {
                cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
                cr.getMetadata().add(HttpHeaders.AUTHORIZATION, "Bearer " + unwrapSupplier(oAuthAccessToken));
            } else {
                cr.getMetadata().remove(HttpHeaders.AUTHORIZATION);
                cr.getHeaders().putSingle("Cookie", "authorization=" + unwrapSupplier(oAuthAccessToken));
                cr.getHeaders().putSingle("X-XSRF-TOKEN", unwrapSupplier(xsrfToken));
            }
        }

        return getNext().handle(cr);
    }

    /**
     * This method allow keep backward compatibility.
     * Now filter use Supplier and cannot guarantee if code provided by supplier will be run with our without
     * Cumulocity context, that method must be resistant to fact if will be run with or without context.
     *
     * java-client can be use without context and this situation is correct and must be equal to situation when
     * java-client is run with constant values for authentication and oauthAccessToken.
     *
     * Try catch block is for situation if someone define context aware oauthAccessToken supplier and execute java-client
     * without Cumulocity context in such case filter will be assume that oauthAccessToken was not provided.
     *
     * In first implementation the value will be null if supplier will have any problem with returning value.
     *
     * @param stringSupplier - the string supplier
     * @return nuwrapped string value
     */
    private String unwrapSupplier(Supplier<String> stringSupplier) {
        try {
            if (stringSupplier == null) {
                return null;
            }
            return stringSupplier.get();
        } catch (Exception e) {
            log.debug("cannot unwrap oauth credentials because: {}", e.getMessage(), e);
            return null;
        }
    }

}
