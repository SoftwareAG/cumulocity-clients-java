package com.cumulocity.sdk.client.notification;

import java.io.IOException;

import org.eclipse.jetty.client.Address;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.security.ProxyAuthorization;
import org.eclipse.jetty.client.security.SimpleRealmResolver;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;

class DefaultHttpClientProvider implements HttpClientProvider {

    private PlatformParameters paramters;

    public DefaultHttpClientProvider(PlatformParameters paramters) {
        this.paramters = paramters;

    }

    public static HttpClientProvider createProvider(PlatformParameters paramters) {
        return new DefaultHttpClientProvider(paramters);
    }

    @Override
    public HttpClient get() throws SDKException {
        try {
            final HttpClient httpClient = new HttpClient();
            configureProxy(httpClient);
            configureAuthentication(httpClient);
            return httpClient;
        } catch (Exception ex) {
            throw new SDKException("unable to create httpclient ", ex);
        }
    }

    private void configureAuthentication(final HttpClient httpClient) {
        httpClient.setRealmResolver(new SimpleRealmResolver(new PlatformPropertiesRealm(paramters)));
    }

    private void configureProxy(HttpClient httpClient) throws SDKException {
        if (hasText(paramters.getProxyHost()) && (paramters.getProxyPort() > 0)) {
            httpClient.setProxy(new Address(paramters.getProxyHost(), paramters.getProxyPort()));
            configureProxyAuthentication(httpClient);
        }

    }

    private void configureProxyAuthentication(HttpClient httpClient) throws SDKException {
        if (hasText(paramters.getProxyUserId()) && hasText(paramters.getProxyPassword())) {
            try {
                httpClient.setProxyAuthentication(new ProxyAuthorization(paramters.getProxyUserId(), paramters.getProxyPassword()));
            } catch (IOException e) {
                throw new SDKException("httpclient proxy configuration failed ", e);
            }
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
