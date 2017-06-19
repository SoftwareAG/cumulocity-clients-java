package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4Handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class CumulocityHttpClientTest {
    
    private static final String HOST = "http://management.cumulocity.com:8080";
    
    private CumulocityHttpClient client;
    
    @Before
    public void setUp() {
        PlatformParameters platformParameters = new PlatformParameters();
        platformParameters.setForceInitialHost(true);
        platformParameters.setHost(HOST);
        client = createClient(platformParameters);
    }

    @Test
    public void shouldChangeHostToPlatformHostIfThisIsForcedInParameters() throws Exception {
        String queryParams = "?test=1&a=1";
        String pathParams = "/test/1/q=1&a=1";

        verifyResolvedPath(HOST, HOST);
        verifyResolvedPath(HOST, "http://127.0.0.1");
        verifyResolvedPath(HOST, "http://127.0.0.1:8181");
        verifyResolvedPath(HOST + queryParams, "http://127.0.0.1:8181" + queryParams);
        verifyResolvedPath(HOST + queryParams, "http://127.0.0.1" + queryParams);
        verifyResolvedPath(HOST + pathParams, "http://127.0.0.1" + pathParams);
    }
    
    private void verifyResolvedPath(String expected, String initial) {
        String resolved = client.resolvePath(initial);
        assertThat(expected, is(resolved));
    }
    
    public static CumulocityHttpClient createClient(PlatformParameters platformParameters) {
        CumulocityHttpClient client = new CumulocityHttpClient(createDefaultClientHander(), mock(ClientConfig.class),null);
        client.setPlatformParameters(platformParameters);
        return client;
    }
    
    private static ApacheHttpClient4Handler createDefaultClientHander() {
        final HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
        return new ApacheHttpClient4Handler(client, null, false);
    }

}
