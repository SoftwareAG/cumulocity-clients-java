package com.cumulocity.sdk.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlatformBuilderTest {

    @Test
    public void shouldBuildPlatformWithCustomConnectionPoolPerHost() {
        Platform platform = PlatformBuilder.platform()
                .withBaseUrl("http://localhost")
                .withConnectionPoolConfigPerHost(120)
                .build();
        assertEquals(HttpClientConfig.httpConfig()
                .pool(ConnectionPoolConfig.connectionPool()
                        .perHost(120)
                        .build())
                .build(), ((PlatformImpl) platform).getHttpClientConfig());
    }

    @Test
    public void shouldBuildPlatformWithCustomHttpReadTimeout() {
        Platform platform = PlatformBuilder.platform()
                .withBaseUrl("http://localhost")
                .withHttpReadTimeout(300000)
                .build();
        assertEquals(HttpClientConfig.httpConfig()
                .httpReadTimeout(300000)
                .build(), ((PlatformImpl) platform).getHttpClientConfig());
    }

    @Test
    public void shouldBuildPlatformWithDefaultHttpClientConfig() {
        Platform platform = PlatformBuilder.platform()
                .withBaseUrl("http://localhost")
                .build();
        assertEquals(HttpClientConfig.httpConfig().build(), ((PlatformImpl) platform).getHttpClientConfig());
    }
}
