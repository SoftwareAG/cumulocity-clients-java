package com.cumulocity.microservice.subscription.annotation;

import com.cumulocity.microservice.agent.server.api.service.MicroserviceRepository;
import com.cumulocity.microservice.subscription.model.core.Credentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@ConditionalOnProperty(value="microservice.subscription.enabled", havingValue="true", matchIfMissing = true)
public class EnableMicroserviceSubscriptionConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PlatformProperties.PlatformPropertiesProvider platformPropertiesProvider() {
        return new PlatformProperties.PlatformPropertiesProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformProperties platformProperties(PlatformProperties.PlatformPropertiesProvider platformPropertiesProvider) {
        return platformPropertiesProvider.platformProperties(null);
    }

    @Bean
    @ConditionalOnMissingBean
    public MicroserviceRepository microserviceRepository(ObjectMapper objectMapper, PlatformProperties properties) {
        final Credentials boostrapUser = properties.getMicroserviceBoostrapUser();
        return MicroserviceRepository.microserviceApi()
                .baseUrlSupplier(properties.getUrl())
                .user(boostrapUser.getName())
                .tenant(boostrapUser.getTenant())
                .password(boostrapUser.getPassword())
                .objectMapper(objectMapper)
                .build();
    }
}
