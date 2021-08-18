package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.annotation.EnableContextSupport;
import com.cumulocity.sdk.client.HttpClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EnableMicroservicePlatformApiConfiguration.class)
@ConfigurationPropertiesScan
@EnableContextSupport
public class HttpClientConfigDefaultPropertyTest extends BaseHttpClientConfigTest {

    @Test
    public void shouldCreateHttpClientConfigBeanUsingProperty() {
        HttpClientConfig expectedHttpClientConfig = HttpClientConfig.httpConfig().build();
        verifyHttpClientConfigBean(expectedHttpClientConfig);
    }

    @Test
    public void shouldCreatePlatformsWithHttpClientConfigWithOverriddenHttpReadTimeout() {
        HttpClientConfig expectedHttpClientConfig = HttpClientConfig.httpConfig().build();

        verifyTenantPlatformHttpClientConfig(expectedHttpClientConfig);
        verifyUserPlatformHttpClientConfig(expectedHttpClientConfig);
    }

}
