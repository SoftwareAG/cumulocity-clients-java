package com.cumulocity.sdk.client;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.Base64;

import java.io.UnsupportedEncodingException;

public class AuthFilters {

    public abstract static class BaseFilter extends ClientFilter {

        protected final String authorization;

        protected BaseFilter(String username, String password, String authPrefix) {
            try {
                String credentials = formatCredentials(username, password, authPrefix);
                this.authorization = authPrefix + " " + new String(Base64.encode(credentials), "ASCII");
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }

        static String formatCredentials(String username, String password, String authPrefix) {
            // for some reason paypal authorization header has format:
            // "Paypal tenant:user:password"
            // while basic authorization header:
            // "Basic tenant/user:password"
            if (CumulocityCredentials.PAYPAL_AUTH_PREFIX.equalsIgnoreCase(authPrefix)) {
                username = username.replace("/", ":");
            }
            return String.format("%s:%s", username, password);

        }
    }

    /**
     * Copied from com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
     */
    public static final class HTTPBasic extends BaseFilter {
        public HTTPBasic(String username, String password, String authPrefix) {
            super(username, password, authPrefix);
        }

        public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
            if (!cr.getMetadata().containsKey("Authorization")) {
                cr.getMetadata().add("Authorization", authorization);
            }

            return this.getNext().handle(cr);
        }
    }

    /**
     * Copied from com.cumulocity.sdk.client.HTTPBasicProxyAuthenticationFilter
     */
    public static final class HTTPBasicProxy extends BaseFilter {

        private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";

        /**
         * Creates a new HTTP Proxy-Authorization filter using provided username
         * and password credentials
         *
         * @param username
         * @param password
         */
        public HTTPBasicProxy(final String username, final String password, String authPrefix) {
            super(username, password, authPrefix);
        }

        @Override
        public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

            if (!cr.getHeaders().containsKey(PROXY_AUTHORIZATION)) {
                cr.getHeaders().putSingle(PROXY_AUTHORIZATION, authorization);
            }
            return getNext().handle(cr);
        }

    }
}
