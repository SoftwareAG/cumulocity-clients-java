package com.cumulocity.microservice.settings.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.settings.TenantOptionPropertySource;
import com.cumulocity.microservice.settings.repository.CurrentApplicationSettingsApi;
import com.cumulocity.microservice.settings.service.EncryptionService;
import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;
import com.cumulocity.microservice.settings.service.impl.EncryptionServiceImpl;
import com.cumulocity.microservice.settings.service.impl.MicroserviceSettingsServiceImpl;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.sdk.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


@ConditionalOnExpression("#{'${microservice.settings.enabled:true}' && environment.containsProperty('C8Y.microservice.isolation')}")
@Configuration
public class EnableTenantOptionSettingsConfiguration {

    @Bean
    @ConditionalOnExpression("#{environment.containsProperty('C8Y.encryptor.password') && environment.containsProperty('C8Y.encryptor.salt')}")
    public EncryptionServiceImpl encryptionService(@Value("${C8Y.encryptor.password}") String encryptorPassword,
                                                   @Value("${C8Y.encryptor.salt}") String encryptorSalt) {
        return new EncryptionServiceImpl(encryptorPassword, encryptorSalt);
    }

    @Bean
    @TenantScope
    @ConditionalOnBean({RestOperations.class, PlatformProperties.class})
    public CurrentApplicationSettingsApi currentApplicationSettingsApi(RestOperations restOperations, PlatformProperties platformProperties) {
        return new CurrentApplicationSettingsApi(restOperations, platformProperties.getUrl());
    }

    @Bean
    @ConditionalOnBean({PlatformProperties.class, ContextService.class, EncryptionService.class, CurrentApplicationSettingsApi.class})
    @ConditionalOnMissingBean
    public MicroserviceSettingsServiceImpl microserviceSettingsService(PlatformProperties platformProperties,
                                                                       ContextService<MicroserviceCredentials> contextService,
                                                                       EncryptionService encryptionService,
                                                                       CurrentApplicationSettingsApi currentApplicationSettingsApi) {
        return new MicroserviceSettingsServiceImpl(platformProperties, contextService, encryptionService, currentApplicationSettingsApi);
    }

    @Bean
    @ConditionalOnBean(MicroserviceSettingsService.class)
    public TenantOptionPropertySource tenantOptionPropertySource(MicroserviceSettingsService microserviceSettingsService) {
        return new TenantOptionPropertySource(microserviceSettingsService);
    }
}
