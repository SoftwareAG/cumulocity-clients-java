package com.cumulocity.microservice.context.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EnableContextSupportConfigurationTest {

    public static class TenantService {
        private static int static_counter = 0;
        private final String tenant;
        private final int counter = TenantService.static_counter ++;

        public TenantService(String tenant) {
            this.tenant = tenant;
        }

        @Override
    public String toString() {
        return tenant + ", " + counter;
    }
}

    @Configuration
    @EnableContextSupport
    public static class TestConfiguration {

        @Autowired
        private ContextService<MicroserviceCredentials> microservice;

        @Bean
        @TenantScope
        public TenantService getContext() {
            return new TenantService(microservice.getContext().getTenant());
        }
    }

    @Autowired
    private ContextService<MicroserviceCredentials> microservice;

    @Autowired
    private TenantService tenantService1;

    @Autowired
    private TenantService tenantService2;

    @Test
    public void shouldCreateTenantServiceOnlyOnceForTheSameTenant() {
        final List<String> services = new ArrayList<>();

        microservice.runWithinContext(new MicroserviceCredentials().withTenant("tenant1"), new Runnable() {
            public void run() {
                services.add(tenantService1.toString());
            }
        });
        microservice.runWithinContext(new MicroserviceCredentials().withTenant("tenant1"), new Runnable() {
            public void run() {
                services.add(tenantService2.toString());
            }
        });

        assertThat(services.get(0)).isEqualTo(services.get(1));
    }

    @Test
    public void shouldCreateDifferentServicesForDifferentTenant() {
        final List<String> services = new ArrayList<>();

        microservice.runWithinContext(new MicroserviceCredentials().withTenant("tenant1"), new Runnable() {
            public void run() {
                services.add(tenantService1.toString());
            }
        });
        microservice.runWithinContext(new MicroserviceCredentials().withTenant("tenant2"), new Runnable() {
            public void run() {
                services.add(tenantService1.toString());
            }
        });

        assertThat(services.get(0)).isEqualTo("tenant1, 0");
        assertThat(services.get(1)).isEqualTo("tenant2, 1");
    }
}
