package com.cumulocity.agent.packaging.uploadMojo.platform.model;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;
import java.util.Set;

@Wither
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenants {
    private List<Tenant> tenants;

    public Set<Tenant> subscribed(Application application) {
        final Set<Tenant> result = Sets.newHashSet();
        for (final Tenant tenant : getTenants()) {
            if (tenant.isSubscribed(application)) {
                result.add(tenant);
            }
        }
        return result;
    }

    public Set<Tenant> byNames(List<String> names) {
        final Set<Tenant> result = Sets.newHashSet();
        for (final Tenant tenant : getTenants()) {
            if (names.contains(tenant.getId())) {
                result.add(tenant);
            }
        }
        return result;
    }
}