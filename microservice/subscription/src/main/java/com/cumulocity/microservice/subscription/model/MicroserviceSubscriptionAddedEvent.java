package com.cumulocity.microservice.subscription.model;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import org.springframework.context.ApplicationEvent;

public class MicroserviceSubscriptionAddedEvent extends ApplicationEvent implements HasCredentials {
    private final MicroserviceCredentials credentials;

    public MicroserviceSubscriptionAddedEvent(MicroserviceCredentials credentials) {
        super(credentials);
        this.credentials = credentials;
    }

    public MicroserviceCredentials getCredentials() {
        return this.credentials;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MicroserviceSubscriptionAddedEvent)) return false;
        final MicroserviceSubscriptionAddedEvent other = (MicroserviceSubscriptionAddedEvent) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$credentials = this.getCredentials();
        final Object other$credentials = other.getCredentials();
        if (this$credentials == null ? other$credentials != null : !this$credentials.equals(other$credentials))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $credentials = this.getCredentials();
        result = result * PRIME + ($credentials == null ? 43 : $credentials.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MicroserviceSubscriptionAddedEvent;
    }

    public String toString() {
        return "MicroserviceSubscriptionAddedEvent(credentials=" + this.getCredentials() + ")";
    }
}
