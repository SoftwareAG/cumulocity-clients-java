package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.annotation.EnableContextSupport;
import com.cumulocity.sdk.client.ConnectionPoolConfig;
import com.cumulocity.sdk.client.HttpClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = EnableMicroservicePlatformApiConfiguration.class)
@ConfigurationPropertiesScan
@TestPropertySource(properties = {
        "C8Y.httpClient.httpReadTimeout=210000",
        "C8Y.httpClient.pool.perHost=120"
})
@EnableContextSupport
public class HttpClientConfigPropertyTest extends BaseHttpClientConfigTest {

    @Test
    public void shouldCreateHttpClientConfigBeanUsingProperty() {
        HttpClientConfig expectedHttpClientConfig = HttpClientConfig.httpConfig()
                .httpReadTimeout(210000)
                .pool(ConnectionPoolConfig.connectionPool()
                        .perHost(120)
                        .build())
                .build();
        verifyHttpClientConfigBean(expectedHttpClientConfig);
    }

    @Test
    public void shouldCreatePlatformsWithDefinedHttpClientConfig() {
        HttpClientConfig expectedHttpClientConfig = HttpClientConfig.httpConfig()
                .httpReadTimeout(210000)
                .pool(ConnectionPoolConfig.connectionPool()
                        .perHost(120)
                        .build())
                .build();

        verifyTenantPlatformHttpClientConfig(expectedHttpClientConfig);
        verifyUserPlatformHttpClientConfig(expectedHttpClientConfig);
    }
}
