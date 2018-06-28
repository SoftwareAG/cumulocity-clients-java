package com.cumulocity.microservice.subscription.repository.application;

import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.RestOperations;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;

public final class CurrentApplicationApi {
    private final RestOperations rest;
    private final ApplicationApiRepresentation api;

    public CurrentApplicationApi(RestOperations rest, ApplicationApiRepresentation api) {
        this.rest = rest;
        this.api = api;
    }

    public ApplicationRepresentation update(ApplicationRepresentation update) {
        return rest.put(api.getCurrentApplication(), APPLICATION, update);
    }

    public ApplicationRepresentation get() {
        return rest.get(api.getCurrentApplication(), APPLICATION, ApplicationRepresentation.class);
    }
}
