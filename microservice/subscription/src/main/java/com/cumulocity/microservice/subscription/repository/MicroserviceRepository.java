package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;

public interface MicroserviceRepository {

    ApplicationRepresentation register(final MicroserviceMetadataRepresentation metadata);

    /**
     * Method is Deprecated. Method {@link this#register(MicroserviceMetadataRepresentation)} should be used instead.
     * Method {@link this#register(String, MicroserviceMetadataRepresentation)} will be removed in the future.
     */
    @Deprecated
    ApplicationRepresentation register(String applicationName, MicroserviceMetadataRepresentation metadata);

    ApplicationRepresentation getCurrentApplication();

    Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId);

}
