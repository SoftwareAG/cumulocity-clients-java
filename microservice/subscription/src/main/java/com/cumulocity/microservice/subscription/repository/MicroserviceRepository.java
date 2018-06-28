package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;

public interface MicroserviceRepository {

    ApplicationRepresentation register(final String applicationName, final MicroserviceMetadataRepresentation metadata);

    Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId);

}
