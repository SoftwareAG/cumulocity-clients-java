package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.agent.server.api.service.MicroserviceRepository;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Optional.*;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections.CollectionUtils.subtract;

@Repository
public class MicroserviceSubscriptionsRepository {
    @java.beans.ConstructorProperties({"repository"})
    @Autowired
    public MicroserviceSubscriptionsRepository(MicroserviceRepository repository) {
        this.repository = repository;
    }

    public Collection<MicroserviceCredentials> getCurrentSubscriptions() {
        return this.currentSubscriptions;
    }

    public static class Subscriptions {
        private final Collection<MicroserviceCredentials> all;
        private final Collection<MicroserviceCredentials> removed;
        private final Collection<MicroserviceCredentials> added;

        @java.beans.ConstructorProperties({"all", "removed", "added"})
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

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Subscriptions)) return false;
            final Subscriptions other = (Subscriptions) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$all = this.getAll();
            final Object other$all = other.getAll();
            if (this$all == null ? other$all != null : !this$all.equals(other$all)) return false;
            final Object this$removed = this.getRemoved();
            final Object other$removed = other.getRemoved();
            if (this$removed == null ? other$removed != null : !this$removed.equals(other$removed)) return false;
            final Object this$added = this.getAdded();
            final Object other$added = other.getAdded();
            if (this$added == null ? other$added != null : !this$added.equals(other$added)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $all = this.getAll();
            result = result * PRIME + ($all == null ? 43 : $all.hashCode());
            final Object $removed = this.getRemoved();
            result = result * PRIME + ($removed == null ? 43 : $removed.hashCode());
            final Object $added = this.getAdded();
            result = result * PRIME + ($added == null ? 43 : $added.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Subscriptions;
        }

        public String toString() {
            return "MicroserviceSubscriptionsRepository.Subscriptions(all=" + this.getAll() + ", removed=" + this.getRemoved() + ", added=" + this.getAdded() + ")";
        }

        public Subscriptions withAll(Collection<MicroserviceCredentials> all) {
            return this.all == all ? this : new Subscriptions(all, this.removed, this.added);
        }

        public Subscriptions withRemoved(Collection<MicroserviceCredentials> removed) {
            return this.removed == removed ? this : new Subscriptions(this.all, removed, this.added);
        }

        public Subscriptions withAdded(Collection<MicroserviceCredentials> added) {
            return this.added == added ? this : new Subscriptions(this.all, this.removed, added);
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

            public String toString() {
                return "MicroserviceSubscriptionsRepository.Subscriptions.SubscriptionsBuilder(all=" + this.all + ", removed=" + this.removed + ", added=" + this.added + ")";
            }
        }
    }

    private final MicroserviceRepository repository;

    private volatile Collection<MicroserviceCredentials> currentSubscriptions = newArrayList();

    public Optional<ApplicationRepresentation> register(String applicationName, MicroserviceMetadataRepresentation metadata) {
        return fromNullable(repository.register(applicationName, metadata));
    }

    public Subscriptions retrieveSubscriptions(String applicationId) {
        final List<MicroserviceCredentials> subscriptions = from(repository.getSubscriptions(applicationId))
                .transform(new Function<ApplicationUserRepresentation, MicroserviceCredentials>() {
                    public MicroserviceCredentials apply(ApplicationUserRepresentation representation) {
                        return MicroserviceCredentials.builder()
                                .username(representation.getName())
                                .tenant(representation.getTenant())
                                .password(representation.getPassword())
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
