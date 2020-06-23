package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.sdk.client.HttpClientConfig;
import com.cumulocity.sdk.client.PlatformImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseHttpClientConfigTest {

    @Autowired
    private CumulocityClientFeature cumulocityClientFeature;

    @Autowired
    @Qualifier("tenantPlatform")
    private PlatformImpl tenantPlatform;

    @Autowired
    @Qualifier("userPlatform")
    private PlatformImpl userPlatform;

    protected void verifyHttpClientConfigBean(HttpClientConfig expectedHttpClientConfig) {
        assertEquals(expectedHttpClientConfig, cumulocityClientFeature.httpClientConfig());
    }

    protected void verifyTenantPlatformHttpClientConfig(HttpClientConfig expectedHttpClientConfig) {
        ContextServiceImpl<MicroserviceCredentials> microserviceCredentialsContextService = new ContextServiceImpl<>(MicroserviceCredentials.class);
        microserviceCredentialsContextService.runWithinContext(new MicroserviceCredentials().withTenant("tenant1"), () -> {
            assertEquals(expectedHttpClientConfig, tenantPlatform.getHttpClientConfig());
        });
    }

    protected void verifyUserPlatformHttpClientConfig(HttpClientConfig expectedHttpClientConfig) {
        ContextServiceImpl<UserCredentials> userCredentialsContextService = new ContextServiceImpl<>(UserCredentials.class);
        userCredentialsContextService.runWithinContext(new UserCredentials().withTenant("tenant2").withUsername("user2"), () -> {
            assertEquals(expectedHttpClientConfig, userPlatform.getHttpClientConfig());
        });
    }
}
