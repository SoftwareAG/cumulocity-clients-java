package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import java.io.UnsupportedEncodingException;

public class AuthFilters {

    /**
     * Copied from com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
     */
    public static final class HTTPBasic extends ClientFilter {
        private final String authentication;

        public HTTPBasic(String username, String password, String authPrefix) {
            try {
                this.authentication = authPrefix + " " + new String(Base64.encode(username + ":" + password), "ASCII");
            } catch (UnsupportedEncodingException var4) {
                throw new RuntimeException(var4);
            }
        }

        public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
            if(!cr.getMetadata().containsKey("Authorization")) {
                cr.getMetadata().add("Authorization", this.authentication);
            }

            return this.getNext().handle(cr);
        }
    }

    /**
     * Copied from com.cumulocity.sdk.client.HTTPBasicProxyAuthenticationFilter
     */
    public static final class HTTPBasicProxy extends ClientFilter {

        private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

        private final String authentication;

        /**
         * Creates a new HTTP Proxy-Authorization filter using provided username
         * and password credentials
         *
         * @param username
         * @param password
         */
        public HTTPBasicProxy(final String username, final String password, String authPrefix) {
            try {
                authentication = authPrefix + " " + new String(Base64.encode(username + ":" + password), "ASCII");
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
}
