package com.cumulocity.sdk.client.proxy;

import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseProxyIT extends JavaSdkITBase {
    protected static final String PROXY_AUTH_USERNAME = "c8y-user";
    protected static final String PROXY_AUTH_PASSWORD = "c8y-password";

    private static ProxyServer proxyServer;

    protected PlatformImpl proxiedPlatform;

    @BeforeAll
    public static void setUpBeforeAll() throws Exception {
        proxyServer = new ProxyServer();
        proxyServer.start();
    }

    @AfterAll
    public static void tearDownAfterAll() throws Exception {
        proxyServer.stop();
    }

    @BeforeEach
    public void setUp() {
        proxiedPlatform = new PlatformImpl(platform.getHost(), platform.getCumulocityCredentials());
        proxiedPlatform.setProxyHost("127.0.0.1");
        proxiedPlatform.setProxyPort(proxyServer.getPort());
        proxiedPlatform.setRequireResponseBody(true);
        proxiedPlatform.setForceInitialHost(true);

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
