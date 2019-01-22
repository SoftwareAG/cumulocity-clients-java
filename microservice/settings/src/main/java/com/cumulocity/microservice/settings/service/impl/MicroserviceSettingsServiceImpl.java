package com.cumulocity.microservice.settings.service.impl;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.settings.repository.CurrentApplicationSettingsApi;
import com.cumulocity.microservice.settings.service.MicroserviceSettingsService;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.CredentialsSwitchingPlatform;
import com.cumulocity.microservice.subscription.repository.DefaultCredentialsSwitchingPlatform;

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.rest.representation.tenant.OptionsRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@RequiredArgsConstructor
public class MicroserviceSettingsServiceImpl implements MicroserviceSettingsService {

    private final PlatformProperties platformProperties;
    private final ContextService<MicroserviceCredentials> contextService;
    private final CredentialsSwitchingPlatform credentialsSwitchingPlatform;

    private final Cache<String, Map<String, String>> cachedSettings = CacheBuilder.newBuilder().expireAfterWrite(10, MINUTES).build();

    @Override
    public Map<String, String> getAll() {
        final Credentials credentials = getCurrentCredentials();
        try {
            return cachedSettings.get(credentials.getTenant(), new Callable<Map<String, String>>() {
                public Map<String, String> call() {
                    return toMap(settingsApi(credentials).findAll());
                }
            });
        } catch (ExecutionException e) {
            log.error("Loading tenant option settings failed: {}", e.getMessage());
            log.debug("Loading tenant option settings failed: {}", e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    @Override
    public <T> T getAs(Class<T> clazz) {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(getAll(), clazz);
    }

    private Map<String, String> toMap(OptionsRepresentation settings) {
        Map<String, String> settingsMap = Maps.newHashMap();
        for (String key : settings.propertyNames()) {
            settingsMap.put(key, settings.getProperty(key));
        }
        return settingsMap;
    }

    private Credentials getCurrentCredentials() {
        if (contextService.isInContext()) {
            return contextService.getContext();
        } else {
            return platformProperties.getMicroserviceBoostrapUser();
        }
    }

    private CurrentApplicationSettingsApi settingsApi(Credentials credentials) {
        CumulocityCredentials cumulocityCredentials = CumulocityBasicCredentials.builder()
                .tenantId(credentials.getTenant())
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .build();
        credentialsSwitchingPlatform.switchTo(cumulocityCredentials);
        return new CurrentApplicationSettingsApi(credentialsSwitchingPlatform.get(), platformProperties.getUrl().get());
    }
}
