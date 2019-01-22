package com.cumulocity.microservice.settings.repository;

import com.cumulocity.rest.representation.tenant.OptionsRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.ws.rs.core.MediaType;

@RequiredArgsConstructor
public class CurrentApplicationSettingsApi {

    private static final String CURRENT_APPLICATION_SETTINGS = "application/currentApplication/settings";

    private final RestOperations rest;
    private final String baseUrl;

    public OptionsRepresentation findAll() {
        return rest.get(url(), MediaType.APPLICATION_JSON_TYPE, OptionsRepresentation.class);
    }

    private String url() {
        final String prefix = StringUtils.trimTrailingCharacter(baseUrl, '/');
        return prefix + "/" + CURRENT_APPLICATION_SETTINGS;
    }
}
