package com.cumulocity.microservice.subscription.service.impl;

import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionsInitializedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 
 * A service which creates a scheduled task to fetch microservice subscriptions.
 * Emits MicroserviceSubscriptionsInitializedEvent when scheduled task is initialized.
 *
 */
@Service
public class MicroserviceSubscriptionScheduler implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MicroserviceSubscriptionScheduler.class);
    private final ScheduledExecutorService subscriptionsWatcher = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
            .setNameFormat("subscriptions-%d")
            .setDaemon(true)
            .build());

    private final MicroserviceSubscriptionsService service;
    private final PlatformProperties properties;
    private final ApplicationEventPublisher eventPublisher;
    private final AtomicBoolean started = new AtomicBoolean(false);

    @Autowired
    public MicroserviceSubscriptionScheduler(
            final MicroserviceSubscriptionsService service,
            final PlatformProperties properties,
            final ApplicationEventPublisher eventPublisher) {
        this.service = service;
        this.properties = properties;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Synchronized
    public void onApplicationEvent(ContextRefreshedEvent applicationContextEvent) {
        if (started.get()) {
            return;
        }
        final int subscriptionDelay = getSubscriptionDelay();
        final int subscriptionInitialDelay = getSubscriptionInitialDelay();
        log.info("Start; subscriptionDelay = {}, subscriptionInitialDelay = {}", subscriptionDelay, subscriptionInitialDelay);
        if (subscriptionDelay <= 0) {
            return;
        }
        subscriptionsWatcher.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    service.subscribe();
                } finally {
                    if (!started.get()) {
                        eventPublisher.publishEvent(new MicroserviceSubscriptionsInitializedEvent());
                    }
                    started.set(true);
                }
            }
        }, subscriptionInitialDelay, subscriptionDelay, MILLISECONDS);
    }

    private int getSubscriptionDelay() {
        return firstNonNull(properties.getSubscriptionDelay(), -1);
    }

    private int getSubscriptionInitialDelay() {
        return firstNonNull(properties.getSubscriptionInitialDelay(), 30_000);
    }
}
