package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.annotation.EnableContextSupport;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EnableMicroservicePlatformApiConfiguration.class)
@ConfigurationPropertiesScan
@EnableContextSupport
class CumulocityClientFeatureTest {

    @Nested
    class TenantPlatform {

        @Autowired
        ContextService<MicroserviceCredentials> tenantContext;

        @Autowired
        Platform tenantPlatform;

        @Autowired
        RestOperations tenantRestOperations;

        @Autowired
        InventoryApi tenantInventoryApi;

        @Test
        void shouldReturnSameInstanceOfAPIsAsAutowired() {
            tenantContext.runWithinContext(new MicroserviceCredentials().withTenant("acme"), () -> {
                assertThat(tenantPlatform.rest()).isNotSameAs(tenantRestOperations);
                assertThat(tenantPlatform.getInventoryApi()).isSameAs(tenantInventoryApi);
            });
            tenantContext.runWithinContext(new MicroserviceCredentials().withTenant("meta"), () -> {
                assertThat(tenantPlatform.rest()).isNotSameAs(tenantRestOperations);
                assertThat(tenantPlatform.getInventoryApi()).isSameAs(tenantInventoryApi);
            });
        }
    }

    @Nested
    class UserPlatform {

        @Autowired
        ContextService<UserCredentials> userContext;

        @Autowired @Qualifier("userPlatform")
        Platform userPlatform;

        @Autowired @Qualifier("userRestConnector")
        RestOperations userRestOperations;

        @Autowired @Qualifier("userInventoryApi")
        InventoryApi userInventoryApi;

        @Test
        void shouldReturnSameInstanceOfAPIsAsAutowired() {
            userContext.runWithinContext(new UserCredentials().withTenant("acme").withUsername("john"), () -> {
                assertThat(userPlatform.rest()).isNotSameAs(userRestOperations);
                assertThat(userPlatform.getInventoryApi()).isSameAs(userInventoryApi);
            });
            userContext.runWithinContext(new UserCredentials().withTenant("acme").withUsername("jane"), () -> {
                assertThat(userPlatform.rest()).isNotSameAs(userRestOperations);
                assertThat(userPlatform.getInventoryApi()).isSameAs(userInventoryApi);
            });
        }
    }
}
