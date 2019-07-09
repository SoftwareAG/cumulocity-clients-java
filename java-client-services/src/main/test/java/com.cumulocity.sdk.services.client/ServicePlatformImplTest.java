package com.cumulocity.sdk.services.client;

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ServicePlatformImplTest {

    @Test
    public void testCreateObjectWhenHostContainsUnderscoreCharacter() {
        String host = "http://test_tenant.test_environment.c8y.io";

        ServicesPlatform platform = new ServicesPlatformImpl(
                host,
                CumulocityBasicCredentials.builder()
                        .username("admin")
                        .tenantId("test_tenant")
                        .password("password")
                        .build()
        );

        assertNotNull(platform);
        assertNotNull(platform.getSmsMessagingApi());
        assertNotNull(platform.getEmailApi());
    }
}
