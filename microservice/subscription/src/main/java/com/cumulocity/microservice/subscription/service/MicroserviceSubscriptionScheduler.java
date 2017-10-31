package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceSubscriptionScheduler implements ApplicationListener<ContextRefreshedEvent> {
    private final ScheduledExecutorService singleThreadScheduler = Executors.newSingleThreadScheduledExecutor();

    private final MicroserviceSubscriptionsService service;
    private final PlatformProperties properties;
    private final AtomicBoolean started = new AtomicBoolean(false);

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
        singleThreadScheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    service.subscribe();
                } finally {
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
