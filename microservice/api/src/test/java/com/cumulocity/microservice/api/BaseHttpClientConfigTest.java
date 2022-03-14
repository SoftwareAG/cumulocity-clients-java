package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.sdk.client.HttpClientConfig;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseHttpClientConfigTest {

    @Autowired
    ContextService<MicroserviceCredentials> microserviceContextService;

    @Autowired
    ContextService<UserCredentials> userContextService;

    @Autowired
    CumulocityClientProperties clientProperties;

    @Autowired
    CumulocityClientFeature.TenantPlatformConfig tenantPlatform;

    @Autowired
    CumulocityClientFeature.UserPlatformConfig userPlatform;

    protected void verifyHttpClientConfigBean(HttpClientConfig expectedHttpClientConfig) {
        assertEquals(expectedHttpClientConfig, clientProperties.getHttpclient());
    }

    protected void verifyTenantPlatformHttpClientConfig(HttpClientConfig expectedHttpClientConfig) {
        microserviceContextService.runWithinContext(new MicroserviceCredentials().withTenant("tenant1"), () -> {
            assertEquals(expectedHttpClientConfig, tenantPlatform.getDelegate().getHttpClientConfig());
        });
    }

    protected void verifyUserPlatformHttpClientConfig(HttpClientConfig expectedHttpClientConfig) {
        userContextService.runWithinContext(new UserCredentials().withTenant("tenant2").withUsername("user2"), () -> {
            assertEquals(expectedHttpClientConfig, userPlatform.getDelegate().getHttpClientConfig());
        });
    }
}
