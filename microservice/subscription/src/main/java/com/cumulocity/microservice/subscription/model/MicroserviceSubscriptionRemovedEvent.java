package com.cumulocity.microservice.subscription.model;

import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * 
 * An event to be emitted with the microservice credentials of newly removed microservice subscription.
 *
 * Note: extends {@link ApplicationEvent} because of spring backwards compatibility.
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

    @Override
    public String toString() {
        return "MicroserviceSubscriptionRemovedEvent{" +
                "tenant='" + tenant + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MicroserviceSubscriptionRemovedEvent that = (MicroserviceSubscriptionRemovedEvent) o;
        return Objects.equals(tenant, that.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant);
    }
}
