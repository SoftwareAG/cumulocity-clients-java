package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Optional.*;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections.CollectionUtils.subtract;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceSubscriptionsRepository {
    @Data
    @Builder
    @Wither
    public static class Subscriptions {
        private final Collection<MicroserviceCredentials> all;
        private final Collection<MicroserviceCredentials> removed;
        private final Collection<MicroserviceCredentials> added;
    }

    private final MicroserviceRepository repository;

    @Getter
    private volatile Collection<MicroserviceCredentials> currentSubscriptions = newArrayList();

    public Optional<ApplicationRepresentation> register(String applicationName, MicroserviceMetadataRepresentation metadata) {
        return fromNullable(repository.register(applicationName, metadata));
    }

    public Subscriptions retrieveSubscriptions(String applicationId) {
        final List<MicroserviceCredentials> subscriptions = from(repository.getSubscriptions(applicationId)).transform(new Function<ApplicationUserRepresentation, MicroserviceCredentials>() {
            public MicroserviceCredentials apply(ApplicationUserRepresentation representation) {
                return MicroserviceCredentials.builder()
                        .username(representation.getName())
                        .tenant(representation.getTenant())
                        .password(representation.getPassword())
                        .build();
            }
        }).toList();
        final Collection<MicroserviceCredentials> removed = subtract(currentSubscriptions, subscriptions);
        final Collection<MicroserviceCredentials> added = subtract(subscriptions, currentSubscriptions);
        return Subscriptions.builder()
                .all(subscriptions)
                .removed(removed)
                .added(added)
                .build();
    }

    public void updateCurrentSubscriptions(final Collection<MicroserviceCredentials> subscriptions) {
        currentSubscriptions = subscriptions;
    }

    public Optional<MicroserviceCredentials> getCredentials(String tenant) {
        for (final MicroserviceCredentials subscription : currentSubscriptions) {
            if (subscription.getTenant().equals(tenant)) {
                return of(subscription);
            }
        }
        return absent();
    }
}
