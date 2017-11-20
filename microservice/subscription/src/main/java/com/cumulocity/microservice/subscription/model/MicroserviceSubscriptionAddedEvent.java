package com.cumulocity.microservice.subscription.model;

import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import com.cumulocity.microservice.subscription.model.core.MicroserviceCredentials;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class MicroserviceSubscriptionAddedEvent extends ApplicationEvent implements HasCredentials {
    private final MicroserviceCredentials credentials;

    public MicroserviceSubscriptionAddedEvent(MicroserviceCredentials credentials) {
        super(credentials);
        this.credentials = credentials;
    }
}
