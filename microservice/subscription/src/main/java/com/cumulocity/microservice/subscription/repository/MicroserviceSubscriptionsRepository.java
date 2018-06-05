package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;

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

            public Subscriptions.SubscriptionsBuilder all(Collection<MicroserviceCredentials> all) {
                this.all = all;
                return this;
            }

            public Subscriptions.SubscriptionsBuilder removed(Collection<MicroserviceCredentials> removed) {
                this.removed = removed;
                return this;
            }

            public Subscriptions.SubscriptionsBuilder added(Collection<MicroserviceCredentials> added) {
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

    public Optional<ApplicationRepresentation> register(String applicationName, MicroserviceMetadataRepresentation metadata) {
        return fromNullable(repository.register(applicationName, metadata));
    }

    public Subscriptions retrieveSubscriptions(String applicationId, final String applicationKey) {
        final List<MicroserviceCredentials> subscriptions = from(repository.getSubscriptions(applicationId))
                .transform(new Function<ApplicationUserRepresentation, MicroserviceCredentials>() {
                    public MicroserviceCredentials apply(ApplicationUserRepresentation representation) {
                        return MicroserviceCredentials.builder()
                                .username(representation.getName())
                                .tenant(representation.getTenant())
                                .password(representation.getPassword())
                                .appKey(applicationKey)
                                .build();
                    }
                })
                .toList();
        final Collection<MicroserviceCredentials> removed = subtract(currentSubscriptions, subscriptions);
        final Collection<MicroserviceCredentials> added = subtract(subscriptions, currentSubscriptions);
        return Subscriptions.builder()
                .all(subscriptions)
                .removed(removed)
                .added(added)
                .build();
    }

    private Collection<MicroserviceCredentials> subtract(Collection<MicroserviceCredentials> a, final Collection<MicroserviceCredentials> b) {
        return FluentIterable.from(a).filter(new Predicate<MicroserviceCredentials>() {
            @Override
            public boolean apply(MicroserviceCredentials credentials) {
                if(b.contains(credentials)){
                    return false;
                }
                return true;
            }
        }).toList();
    }

    public void updateCurrentSubscriptions(final Collection<MicroserviceCredentials> subscriptions) {
        currentSubscriptions = subscriptions;
    }
}
