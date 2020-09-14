package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.beans.ConstructorProperties;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;

@Repository
public class MicroserviceSubscriptionsRepository {
    @ConstructorProperties({"repository"})
    @Autowired
    public MicroserviceSubscriptionsRepository(MicroserviceRepository repository) {
        this.repository = repository;
    }

    public Collection<MicroserviceCredentials> getCurrentSubscriptions() {
        return this.currentSubscriptions;
    }

    @ToString
    public static class Subscriptions {
        private final Collection<MicroserviceCredentials> all;
        private final Collection<MicroserviceCredentials> removed;
        private final Collection<MicroserviceCredentials> added;

        @ConstructorProperties({"all", "removed", "added"})
        Subscriptions(Collection<MicroserviceCredentials> all, Collection<MicroserviceCredentials> removed, Collection<MicroserviceCredentials> added) {
            this.all = all;
            this.removed = removed;
            this.added = added;
        }

        public static SubscriptionsBuilder builder() {
            return new SubscriptionsBuilder();
        }

        public Collection<MicroserviceCredentials> getAll() {
            return this.all;
        }

        public Collection<MicroserviceCredentials> getRemoved() {
            return this.removed;
        }

        public Collection<MicroserviceCredentials> getAdded() {
            return this.added;
        }

        public static class SubscriptionsBuilder {
            private Collection<MicroserviceCredentials> all;
            private Collection<MicroserviceCredentials> removed;
            private Collection<MicroserviceCredentials> added;

            SubscriptionsBuilder() {
            }

            public SubscriptionsBuilder all(Collection<MicroserviceCredentials> all) {
                this.all = all;
                return this;
            }

            public SubscriptionsBuilder removed(Collection<MicroserviceCredentials> removed) {
                this.removed = removed;
                return this;
            }

            public SubscriptionsBuilder added(Collection<MicroserviceCredentials> added) {
                this.added = added;
                return this;
            }

            public Subscriptions build() {
                return new Subscriptions(all, removed, added);
            }
        }
    }

    private final MicroserviceRepository repository;

    private volatile Collection<MicroserviceCredentials> currentSubscriptions = newArrayList();

    public Optional<ApplicationRepresentation> register(MicroserviceMetadataRepresentation metadata) {
        return ofNullable(repository.register(metadata));
    }

    @Deprecated
    public Optional<ApplicationRepresentation> register(String applicationName, MicroserviceMetadataRepresentation metadata) {
        return ofNullable(repository.register(applicationName, metadata));
    }

    public Subscriptions retrieveSubscriptions(String applicationId) {
        List<MicroserviceCredentials> subscriptions = StreamSupport.stream(repository.getSubscriptions(applicationId).spliterator(), false).map(representation -> MicroserviceCredentials.builder()
                .username(representation.getName())
                .tenant(representation.getTenant())
                .password(representation.getPassword())
                .oAuthAccessToken(null)
                .xsrfToken(null)
                .build()).collect(Collectors.toCollection(ArrayList::new));
        moveManagementToFront(subscriptions);
        return diffWithCurrentSubscriptions(subscriptions);
    }

    private void moveManagementToFront(List<MicroserviceCredentials> subscriptions) {
        if (CollectionUtils.size(subscriptions) < 2) {
            return;
        }
        MicroserviceCredentials management = null;
        for (MicroserviceCredentials subscription : subscriptions) {
            if ("management".equals(subscription.getTenant())) {
                management = subscription;
                break;
            }
        }
        if (management != null) {
            subscriptions.remove(management);
            subscriptions.add(0, management);
        }
    }

    public Subscriptions diffWithCurrentSubscriptions(List<MicroserviceCredentials> credentials) {
        final Collection<MicroserviceCredentials> removed = subtract(currentSubscriptions, credentials);
        final Collection<MicroserviceCredentials> added = subtract(credentials, currentSubscriptions);
        return Subscriptions.builder()
                .all(credentials)
                .removed(removed)
                .added(added)
                .build();
    }

    private Collection<MicroserviceCredentials> subtract(Collection<MicroserviceCredentials> a, final Collection<MicroserviceCredentials> b) {
        return a.stream().filter(credentials -> {
            if (b.contains(credentials)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    public void updateCurrentSubscriptions(final Collection<MicroserviceCredentials> subscriptions) {
        currentSubscriptions = subscriptions;
    }
}
