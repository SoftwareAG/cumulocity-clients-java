package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;

public interface MicroserviceRepository {

    ApplicationRepresentation register(final MicroserviceMetadataRepresentation metadata);

    /**
     * Method is Deprecated and will be removed in the future
     * Use {@link #register(MicroserviceMetadataRepresentation)} method instead.
     * @param applicationName application name
     * @param metadata microservice metadata
     * @return application representation
     */
    @Deprecated
    ApplicationRepresentation register(String applicationName, MicroserviceMetadataRepresentation metadata);

    ApplicationRepresentation getCurrentApplication();

    Iterable<ApplicationUserRepresentation> getSubscriptions();

    /**
     * Method is Deprecated and will be removed in the future.
     * Use {@link #getSubscriptions()} method instead.
     * @param applicationId application identifier
     * @return collection of application users
     */
    @Deprecated
    Iterable<ApplicationUserRepresentation> getSubscriptions(String applicationId);

}
