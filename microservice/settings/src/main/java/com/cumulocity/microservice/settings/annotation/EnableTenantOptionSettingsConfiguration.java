package com.cumulocity.microservice.settings.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.settings.TenantOptionPropertySource;
import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;
import com.cumulocity.microservice.settings.service.impl.MicroserviceSettingsServiceImpl;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.CredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.DefaultCredentialsSwitchingPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


@ConditionalOnExpression("#{'${microservice.options.enabled:true}' && environment.containsProperty('C8Y.microservice.isolation')}")
@Configuration
public class EnableTenantOptionSettingsConfiguration {

    @Bean
    @ConditionalOnBean({PlatformProperties.class, ContextService.class})
    @ConditionalOnMissingBean
    public MicroserviceSettingsServiceImpl microserviceSettingsService(PlatformProperties platformProperties, ContextService<MicroserviceCredentials> contextService) {
        CredentialsSwitchingPlatform credentialsSwitchingPlatform =  new DefaultCredentialsSwitchingPlatform(platformProperties.getUrl());
        return new MicroserviceSettingsServiceImpl(platformProperties, contextService, credentialsSwitchingPlatform);
    }

    @Bean
    @ConditionalOnBean(MicroserviceSettingsService.class)
    public TenantOptionPropertySource tenantOptionPropertySource(MicroserviceSettingsService microserviceSettingsService) {
        return new TenantOptionPropertySource(microserviceSettingsService);
    }
}
