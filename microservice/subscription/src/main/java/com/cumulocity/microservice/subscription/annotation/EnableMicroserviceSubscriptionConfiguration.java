package com.cumulocity.microservice.subscription.annotation;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.properties.ConfigurationFileProvider;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.DefaultCredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceRepositoryBuilder;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.JSONBase;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.application.MicroserviceManifestRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
                        .switchTo(CumulocityCredentials.builder()
                                .username(boostrapUser.getUsername())
                                .password(boostrapUser.getPassword())
                                .tenantId(boostrapUser.getTenant())
                                .buildBasic()))
                .objectMapper(objectMapper)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public MicroserviceMetadataRepresentation metadata(Environment environment) throws IOException {
        ConfigurationFileProvider provider = new ConfigurationFileProvider(environment);

        final Iterable<Path> manifests = provider.find(new String[]{"cumulocity"}, ".json");
        if(!Iterables.isEmpty(manifests)){
            try(final BufferedReader reader = Files.newBufferedReader(Iterables.getFirst(manifests, null), Charsets.UTF_8)){
                final MicroserviceManifestRepresentation manifest = JSONBase.fromJSON(reader, MicroserviceManifestRepresentation.class);
                return MicroserviceMetadataRepresentation.microserviceMetadataRepresentation()
                        .requiredRoles(Objects.firstNonNull(manifest.getRequiredRoles(), ImmutableList.<String>of()))
                        .roles(Objects.firstNonNull(manifest.getRoles(), ImmutableList.<String>of()))
                        .build();
            }
        }

        return new MicroserviceMetadataRepresentation();
    }

    @Bean
    @TenantScope
    @ConditionalOnMissingBean
    public ApplicationApi applicationApi(RestOperations restOperations) {
        return new ApplicationApi(restOperations);
    }
}
