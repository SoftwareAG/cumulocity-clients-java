package com.cumulocity.microservice.settings.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.settings.TenantOptionPropertySource;
import com.cumulocity.microservice.settings.repository.CurrentApplicationSettingsApi;
import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;
import com.cumulocity.microservice.settings.service.impl.MicroserviceSettingsServiceImpl;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.sdk.client.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


@ConditionalOnExpression("#{'${microservice.settings.enabled:true}' && environment.containsProperty('C8Y.microservice.isolation')}")
@Configuration
public class EnableTenantOptionSettingsConfiguration {

    @Bean
    @TenantScope
    @ConditionalOnBean({RestOperations.class, PlatformProperties.class})
    public CurrentApplicationSettingsApi currentApplicationSettingsApi(RestOperations restOperations, PlatformProperties platformProperties) {
        return new CurrentApplicationSettingsApi(restOperations, platformProperties.getUrl());
    }

    @Bean
    @ConditionalOnBean({PlatformProperties.class, ContextService.class, CurrentApplicationSettingsApi.class})
    @ConditionalOnMissingBean
    public MicroserviceSettingsServiceImpl microserviceSettingsService(PlatformProperties platformProperties,
                                                                       ContextService<MicroserviceCredentials> contextService,
                                                                       CurrentApplicationSettingsApi currentApplicationSettingsApi) {
        return new MicroserviceSettingsServiceImpl(platformProperties, contextService, currentApplicationSettingsApi);
    }

    @Bean
    @ConditionalOnBean(MicroserviceSettingsService.class)
    public TenantOptionPropertySource tenantOptionPropertySource(MicroserviceSettingsService microserviceSettingsService) {
        return new TenantOptionPropertySource(microserviceSettingsService);
    }
}
