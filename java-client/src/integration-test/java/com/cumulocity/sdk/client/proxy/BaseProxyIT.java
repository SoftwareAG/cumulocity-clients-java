package com.cumulocity.sdk.client.proxy;

import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class BaseProxyIT extends JavaSdkITBase {
    protected static final String PROXY_AUTH_USERNAME = "c8y-user";
    protected static final String PROXY_AUTH_PASSWORD = "c8y-password";

    private static ProxyServer proxyServer;

    protected PlatformImpl proxiedPlatform;

    @BeforeClass
    public static void setUpBeforeAll() throws Exception {
        proxyServer = new ProxyServer();
        proxyServer.start();
    }

    @AfterClass
    public static void tearDownAfterAll() throws Exception {
        proxyServer.stop();
    }

    @Before
    public void setUp() {
        proxiedPlatform = new PlatformImpl(platform.getHost(), platform.getCumulocityCredentials());
        proxiedPlatform.setProxyHost("127.0.0.1");
        proxiedPlatform.setProxyPort(proxyServer.getPort());
        proxiedPlatform.setRequireResponseBody(true);

        proxyServer.setBasicAuthUsername(null);
        proxyServer.setBasicAuthPassword(null);
    }

    protected void givenAuthenticatedProxyAndProxiedPlatform(String username, String password) {
        givenAuthenticatedProxy(username, password);
        givenAuthenticatedProxiedPlatform(username, password);
    }

    protected void givenAuthenticatedProxy(String username, String password) {
        proxyServer.setBasicAuthUsername(username);
        proxyServer.setBasicAuthPassword(password);
    }

    protected void givenAuthenticatedProxiedPlatform(String username, String password) {
        proxiedPlatform.setProxyUserId(username);
        proxiedPlatform.setProxyPassword(password);
    }
}
