package com.cumulocity.sdk.services.client;

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import org.junit.Test;


public class ServicePlatformImplTest {

    @Test
    public void shouldCreateObjectWhenAddressContainsUnderscoreCharacter() {
        String host = "http://test_tenant.test_environment.c8y.io";

        ServicesPlatform platform = sampleServicesPlatform(host);
    }

    @Test
    public void shouldGetApisForGivenPlatformAddress() {
        String host = "http://test-tenant.test-environment.c8y.io";

        ServicesPlatform platform = sampleServicesPlatform(host);

        platform.getSmsMessagingApi();
        platform.getEmailApi();
    }

    private ServicesPlatform sampleServicesPlatform(String host) {
        return new ServicesPlatformImpl(
                host,
                CumulocityBasicCredentials.builder()
                        .username("admin")
                        .password("password")
                        .tenantId("test")
                        .build()
        );
    }
}
