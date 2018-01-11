package com.cumulocity.microservice.subscription.annotation;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.DefaultCredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.cumulocity.model.authentication.CumulocityCredentials.Builder.cumulocityCredentials;

@Configuration
@ComponentScan(basePackageClasses = {
        MicroserviceSubscriptionsService.class,
        MicroserviceSubscriptionsRepository.class,
})
@ConditionalOnProperty(value = "microservice.subscription.enabled", havingValue = "true", matchIfMissing = true)
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
    public MicroserviceRepository microserviceRepository(ObjectMapper objectMapper, final PlatformProperties properties) {
        final Credentials boostrapUser = properties.getMicroserviceBoostrapUser();
        return MicroserviceRepository.microserviceApi()
                .baseUrl(properties.getUrl())
                .connector(new DefaultCredentialsSwitchingPlatform(properties.getUrl())
                        .switchTo(cumulocityCredentials(boostrapUser.getUsername(), boostrapUser.getPassword())
                                .withTenantId(boostrapUser.getTenant())
                                .build()))
                .objectMapper(objectMapper)
                // When no isolation level defined then application must be registered
                .register(properties.getIsolation() == null)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public MicroserviceMetadataRepresentation metadata() {
        return new MicroserviceMetadataRepresentation();
    }
}
