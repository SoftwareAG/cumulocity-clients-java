package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionRemovedEvent;
import com.cumulocity.microservice.subscription.model.core.Credentials;
import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import com.cumulocity.microservice.subscription.model.core.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository.Subscriptions;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.FluentIterable.from;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceSubscriptionsService {

    public interface MicroserviceChangedListener<T extends HasCredentials> {
//        returns true if subscription is properly added
        boolean apply(T event) throws Exception;
    }

    private final PlatformProperties properties;
    private final ApplicationEventPublisher eventPublisher;
    private final MicroserviceSubscriptionsRepository repository;
    private final MicroserviceMetadataRepresentation microserviceMetadataRepresentation;
    private volatile Credentials processed;
    private final List<MicroserviceChangedListener> listeners = Lists.<MicroserviceChangedListener>newArrayList(
            new MicroserviceChangedListener() {
                public boolean apply(HasCredentials event) {
                    try {
//                        these are two different methods
                        if (event instanceof ApplicationEvent) {
                            eventPublisher.publishEvent((ApplicationEvent) event);
                        } else {
                            eventPublisher.publishEvent(event);
                        }
                        return true;
                    } catch (final Exception ex) {
                        log.error(ex.getMessage(), ex);
                        return false;
                    }
                }
            }
    );

    @Synchronized
    public void listen(MicroserviceChangedListener listener) {
        this.listeners.add(listener);
        for (final MicroserviceCredentials user : repository.getCurrentSubscriptions()) {
            try {
                processed = user;
                listener.apply(new MicroserviceSubscriptionAddedEvent(user));
            } catch (final Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                processed = null;
            }
        }
    }

    @Synchronized
    public <T extends HasCredentials> void listen(final Class<T> clazz, final MicroserviceChangedListener<T> listener) {
        listen(new MicroserviceChangedListener() {
            @Override
            public boolean apply(HasCredentials event) throws Exception {
                if (clazz.isInstance(event)) {
                    return listener.apply((T) event);
            }
                return true;
            }
        });
    }

    @Synchronized
    public void subscribe() {
        try {
            final Optional<ApplicationRepresentation> application = repository.register(properties.getApplicationName(), convert(microserviceMetadataRepresentation));
            if (application.isPresent()) {
                final Subscriptions subscriptions = repository.retrieveSubscriptions(application.get().getId());

                 from(subscriptions.getRemoved()).filter(new Predicate<MicroserviceCredentials>() {
                    public boolean apply(final MicroserviceCredentials user) {
                        logIfNotFirstTime("Remove subscription: {}", user);

                        for (final MicroserviceChangedListener listener : listeners) {
                            try {
                                if (!listener.apply(new MicroserviceSubscriptionRemovedEvent(user))) {
                                    return false;
                                }
                            } catch (final Exception ex) {
                                log.error(ex.getMessage(), ex);
                                return false;
                            }
                        }
                        return true;
                    }
                }).toList();

                final ImmutableList<MicroserviceCredentials> successfullyAdded = from(subscriptions.getAdded()).filter(new Predicate<MicroserviceCredentials>() {
                    public boolean apply(final MicroserviceCredentials user) {
                        logIfNotFirstTime("Add subscription: {}", user);

                        for (final MicroserviceChangedListener listener : listeners) {
                            try {
                                processed = user;
                                if (!listener.apply(new MicroserviceSubscriptionAddedEvent(user))) {
                                    return false;
                                }
                            } catch (final Exception ex) {
                                log.error(ex.getMessage(), ex);
                                return false;
                            } finally {
                                processed = null;
                            }
                        }
                        return true;
                    }
                }).toList();

                repository.updateCurrentSubscriptions(from(subscriptions.getAll())
                        .filter(new Predicate<MicroserviceCredentials>() {
                            public boolean apply(MicroserviceCredentials user) {
                                if (subscriptions.getAdded().contains(user)) {
                                    return successfullyAdded.contains(user);
                                }
                                return true;
                            }
                        })
                        .toList());
            } else {
                log.error("Application {} not found", properties.getApplicationName());
            }
        } catch (Throwable e) {
            log.error("Error while reacting on microservice subscription", e);
        }
    }

    public Collection<MicroserviceCredentials> getAll() {
        return repository.getCurrentSubscriptions();
    }

    /**
     * This method should not be invoked in listener because it will return previous state
     */
    public Optional<MicroserviceCredentials> getCredentials(String tenant) {
        for (final MicroserviceCredentials subscription : repository.getCurrentSubscriptions()) {
            if (subscription.getTenant().equals(tenant)) {
                return of(subscription);
            }
        }
        if (processed != null) {
            if (processed.getTenant().equals(tenant)) {
                return Optional.of((MicroserviceCredentials) processed);
            }
        }
        return absent();
    }

    private void logIfNotFirstTime(String s, MicroserviceCredentials user) {
        String newPassword = user.getPassword();
        if (newPassword != null && newPassword.length() > 3) {
            newPassword = newPassword.substring(0, 2) + "*******";
        }
        log.debug(s, user.withPassword(newPassword));
    }

    /**
     * Now MicroserviceRepository is based on MicroserviceMetadata object from cumulocity model.
     * But it shouldn't be because now we have to import here cumulocity model with all its transient dependencies which causes problems (we have to exclude some junk).
     */
    private com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation convert(MicroserviceMetadataRepresentation meta) {
        com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation.MicroserviceMetadataRepresentationBuilder builder = com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation();
        if (meta != null) {
            if (meta.getRequiredRoles() != null) {
                builder.requiredRoles(meta.getRequiredRoles());
            }
            if (meta.getRoles() != null) {
                builder.roles(meta.getRoles());
            }
            if (meta.getUrl() != null) {
                builder.url(meta.getUrl());
            }
        }
        return builder.build();
    }
}
