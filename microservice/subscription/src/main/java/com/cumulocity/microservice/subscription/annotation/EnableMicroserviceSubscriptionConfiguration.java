package com.cumulocity.microservice.subscription.annotation;

import com.cumulocity.microservice.agent.server.api.service.MicroserviceRepository;
import com.cumulocity.microservice.agent.server.api.service.SelfRegistration;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformBuilder;
import com.cumulocity.sdk.client.RestOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        MicroserviceSubscriptionsService.class,
        MicroserviceSubscriptionsRepository.class,
        MicroserviceRepository.class
})
@ConditionalOnProperty(value = "microservice.subscription.enabled", havingValue = "true", matchIfMissing = true)
public class EnableMicroserviceSubscriptionConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PlatformProperties.PlatformPropertiesProvider platformPropertiesProvider(SelfRegistration selfRegistration) {
        return new PlatformProperties.PlatformPropertiesProvider(selfRegistration);
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformProperties platformProperties(PlatformProperties.PlatformPropertiesProvider platformPropertiesProvider) {
        return platformPropertiesProvider.platformProperties(null);
    }

    @Bean
    @ConditionalOnMissingBean
    public MicroserviceRepository microserviceRepository(ObjectMapper objectMapper, final PlatformProperties properties) {
        final Credentials boostrapUser = properties.getMicroserviceBoostrapUser();
        return MicroserviceRepository.microserviceApi()
                .baseUrl(properties.getUrl())
                .connector(new Supplier<RestOperations>() {
                    @Override
                    public RestOperations get() {
                        try (Platform platform = PlatformBuilder.platform()
                                .withBaseUrl(properties.getUrl().get())
                                .withUsername(boostrapUser.getUsername())
                                .withPassword(boostrapUser.getPassword())
                                .withTenant(boostrapUser.getTenant())
                                .withForceInitialHost(properties.getForceInitialHost())
                                .build()) {
                            return platform.rest();
                        }
                    }
                })
                .objectMapper(objectMapper)
                // When no isolation level defined then application must be registered
                .register(properties.getIsolation() == null)
                .build();
    }


    @Bean
    public SelfRegistration selfRegistration() {
        return new SelfRegistration();
    }
}
