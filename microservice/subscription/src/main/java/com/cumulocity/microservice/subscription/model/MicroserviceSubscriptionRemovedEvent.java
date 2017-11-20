package com.cumulocity.microservice.subscription.model;

import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import com.cumulocity.microservice.subscription.model.core.MicroserviceCredentials;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class MicroserviceSubscriptionRemovedEvent extends ApplicationEvent implements HasCredentials {
    private final MicroserviceCredentials credentials;

    public MicroserviceSubscriptionRemovedEvent(MicroserviceCredentials credentials) {
        super(credentials);
        this.credentials = credentials;
    }
}
