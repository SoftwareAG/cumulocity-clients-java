package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class ApplicationWithSubscriptions {
    private final Application application;
    private final Set<Tenant> subscriptions;

    public ApplicationWithSubscriptions(Application application) {
        this(application, Sets.<Tenant>newHashSet());
    }

    public Set<Tenant> getSubscriptions() {
        return subscriptions;
    }

    public void addSubscriptions(Collection<Tenant> tenants) {
        subscriptions.addAll(tenants);
    }
}
