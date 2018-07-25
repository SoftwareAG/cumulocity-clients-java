package com.cumulocity.microservice.subscription.annotation;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.DefaultCredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.sdk.client.RestOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
    public MicroserviceRepository microserviceRepository(ObjectMapper objectMapper, final PlatformProperties properties, final Environment environment) {
        final Credentials boostrapUser = properties.getMicroserviceBoostrapUser();
        return MicroserviceRepositoryBuilder.microserviceRepositoryBuilder()
                .baseUrl(properties.getUrl())
                .environment(environment)
                .connector(new DefaultCredentialsSwitchingPlatform(properties.getUrl())
                        .switchTo(cumulocityCredentials(boostrapUser.getUsername(), boostrapUser.getPassword())
                                .withTenantId(boostrapUser.getTenant())
                                .build()))
                .objectMapper(objectMapper)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public MicroserviceMetadataRepresentation metadata() {
        return new MicroserviceMetadataRepresentation();
    }

    @Bean
    @TenantScope
    @ConditionalOnMissingBean
    public ApplicationApi applicationApi(RestOperations restOperations) {
        return new ApplicationApi(restOperations);
    }
}
