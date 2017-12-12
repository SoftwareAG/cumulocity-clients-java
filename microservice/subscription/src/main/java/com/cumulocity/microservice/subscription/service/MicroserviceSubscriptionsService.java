package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Optional;

import java.util.Collection;

public interface MicroserviceSubscriptionsService {

    Collection<MicroserviceCredentials> getAll();

    Optional<MicroserviceCredentials> getCredentials(String tenant);

    void subscribe();
}
