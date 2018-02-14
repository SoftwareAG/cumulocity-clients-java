package com.cumulocity.microservice.subscription.service.impl;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionRemovedEvent;
import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.FluentIterable.from;

@Service
public class MicroserviceSubscriptionsServiceImpl implements MicroserviceSubscriptionsService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MicroserviceSubscriptionsService.class);

    public interface MicroserviceChangedListener<T extends HasCredentials> {
        boolean apply(T event) throws Exception;
    }

    private final PlatformProperties properties;
    private final ApplicationEventPublisher eventPublisher;
    private final MicroserviceSubscriptionsRepository repository;
    private final MicroserviceMetadataRepresentation microserviceMetadataRepresentation;
    private final ContextService<MicroserviceCredentials> contextService;

    private volatile Credentials processed;
    private final List<MicroserviceChangedListener> listeners = Lists.<MicroserviceChangedListener>newArrayList(
            new MicroserviceChangedListener() {
                public boolean apply(HasCredentials event) {
                    try {
                        if (event instanceof ApplicationEvent) {
//                            backwards compatibility - in older spring version there was no publishEvent(Object) method
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

    @Autowired
    public MicroserviceSubscriptionsServiceImpl(
            final PlatformProperties properties,
            final ApplicationEventPublisher eventPublisher,
            final MicroserviceSubscriptionsRepository repository,
            final MicroserviceMetadataRepresentation microserviceMetadataRepresentation,
            final ContextService<MicroserviceCredentials> contextService) {
        this.properties = properties;
        this.eventPublisher = eventPublisher;
        this.repository = repository;
        this.microserviceMetadataRepresentation = microserviceMetadataRepresentation;
        this.contextService = contextService;
    }

    @Synchronized
    public void listen(MicroserviceChangedListener listener) {
        this.listeners.add(listener);
        for (final MicroserviceCredentials user : repository.getCurrentSubscriptions()) {
            invokeAdded(user, listener);
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

    @Override
    @Synchronized
    public void subscribe() {
        try {
            final Optional<ApplicationRepresentation> application = repository.register(properties.getApplicationName(), microserviceMetadataRepresentation);
            if (application.isPresent()) {
                final MicroserviceSubscriptionsRepository.Subscriptions subscriptions = repository.retrieveSubscriptions(application.get().getId());

                from(subscriptions.getRemoved()).filter(new Predicate<MicroserviceCredentials>() {
                    public boolean apply(final MicroserviceCredentials user) {
                        log("Remove subscription: {}", user);

                        for (final MicroserviceChangedListener listener : listeners) {
                            if (!invokeRemoved(user, listener)) {
                                return false;
                            }
                        }
                        return true;
                    }
                }).toList();

                final ImmutableList<MicroserviceCredentials> successfullyAdded = from(subscriptions.getAdded()).filter(new Predicate<MicroserviceCredentials>() {
                    public boolean apply(final MicroserviceCredentials user) {
                        log("Add subscription: {}", user);

                        for (final MicroserviceChangedListener listener : listeners) {
                            if (!invokeAdded(user, listener)) {
                                return false;
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

    private boolean invokeRemoved(MicroserviceCredentials user, MicroserviceChangedListener listener) {
        try {
            if (!listener.apply(new MicroserviceSubscriptionRemovedEvent(user))) {
                return false;
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    private boolean invokeAdded(final MicroserviceCredentials user, final MicroserviceChangedListener listener) {
        try {
            processed = user;

            final boolean successful = contextService.callWithinContext(user, new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return listener.apply(new MicroserviceSubscriptionAddedEvent(user));
                }
            });

            if (!successful) {
                return false;
            }
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            return false;
        } finally {
            processed = null;
        }
        return true;
    }

    @Override
    @Synchronized
    public Collection<MicroserviceCredentials> getAll() {
        return repository.getCurrentSubscriptions();
    }

    @Override
    @Synchronized
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

    @Override
    public String getTenant() {
        return contextService.getContext().getTenant();
    }

    @Override
    public void runForEachTenant(final Runnable runnable) {
        for (final MicroserviceCredentials credentials : getAll()) {
            contextService.runWithinContext(credentials, runnable);
        }
    }

    @Override
    public void runForTenant(String tenant, final Runnable runnable) {
        for (final MicroserviceCredentials credentials : getCredentials(tenant).asSet()) {
            contextService.runWithinContext(credentials, runnable);
        }
    }

    private void log(String s, MicroserviceCredentials user) {
        String newPassword = user.getPassword();
        if (newPassword != null && newPassword.length() > 3) {
            newPassword = newPassword.substring(0, 2) + "*******";
        }
        log.debug(s, user.withPassword(newPassword));
    }
}
