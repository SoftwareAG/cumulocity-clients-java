package com.cumulocity.microservice.subscription.repository.application;

import com.cumulocity.rest.representation.application.ApplicationCollectionRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.google.common.base.Optional;
import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION_COLLECTION;
import static com.cumulocity.rest.representation.user.UserMediaType.USER;

@Slf4j
public class ApplicationApi {
    private final RestOperations rest;
    private final ApplicationApiRepresentation microserviceApi;

    public ApplicationApi(RestOperations rest) {
        this(rest, ApplicationApiRepresentation.of(Suppliers.ofInstance("")));
    }

    public ApplicationApi(RestOperations rest, ApplicationApiRepresentation microserviceApi) {
        this.rest = rest;
        this.microserviceApi = microserviceApi;
    }

    public ApplicationCollectionRepresentation list() {
        return rest.get(microserviceApi.getCollectionUrl(), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
    }

    public Optional<ApplicationRepresentation> getByName(String name) {
        final ApplicationCollectionRepresentation response = rest.get(microserviceApi.getFindByNameUrl(name), APPLICATION_COLLECTION, ApplicationCollectionRepresentation.class);
        if (response.getApplications().size() == 1) {
            return Optional.of(response.getApplications().get(0));
        }
        return Optional.absent();
    }

    public ApplicationRepresentation create(ApplicationRepresentation application) {
        return rest.post(microserviceApi.getCollectionUrl(), APPLICATION, application);
    }


    public ApplicationUserRepresentation getBootstrapUser(String applicationId) {
        return rest.get(microserviceApi.getBootstrapUserUrl(applicationId), USER, ApplicationUserRepresentation.class);
    }

    public ApplicationRepresentation update(ApplicationRepresentation update) {
        return rest.put(microserviceApi.getByIdUrl(update.getId()), APPLICATION, update);
    }

    public CurrentApplicationApi currentApplication() {
        return new CurrentApplicationApi(rest, microserviceApi);
    }
}
