package com.cumulocity.microservice.subscription.model;

import org.springframework.context.ApplicationEvent;

/**
 * 
 * An event to be emitted with the microservice credentials of newly removed microservice subscription.
 *
 */
public class MicroserviceSubscriptionRemovedEvent extends ApplicationEvent {
    private final String tenant;

    public MicroserviceSubscriptionRemovedEvent(String tenant) {
        super(tenant);
        this.tenant = tenant;
    }

    public String getTenant() {
        return this.tenant;
    }
}
