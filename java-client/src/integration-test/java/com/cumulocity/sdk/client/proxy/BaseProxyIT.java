package com.cumulocity.sdk.client.proxy;

import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class BaseProxyIT extends JavaSdkITBase {
    private static final int PROXY_PORT = 8888;
    protected static final String PROXY_AUTH_USERNAME = "c8y-user";
    protected static final String PROXY_AUTH_PASSWORD = "c8y-password";

    private static ProxyServer proxyServer;

    protected PlatformImpl proxiedPlatform;

    @BeforeClass
    public static void setUpBeforeAll() throws Exception {
        proxyServer = new ProxyServer();
        proxyServer.setPort(PROXY_PORT);
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
        proxiedPlatform.setProxyPort(PROXY_PORT);
        proxiedPlatform.setRequireResponseBody(true);
    }

    @After
    public void tearDown() {
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
