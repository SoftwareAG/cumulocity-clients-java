package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.RestOperations;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;

final class CurrentApplicationApi {
    private final RestOperations rest;
    private final MicroserviceApiRepresentation api;

    public CurrentApplicationApi(RestOperations rest, MicroserviceApiRepresentation api) {
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
