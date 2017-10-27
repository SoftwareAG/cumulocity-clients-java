package com.cumulocity.microservice.subscription.service;

import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.google.common.base.MoreObjects;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceSubscriptionScheduler {
    private final ScheduledExecutorService singleThreadScheduler = Executors.newSingleThreadScheduledExecutor();

    private final MicroserviceSubscriptionsService service;
    private final PlatformProperties properties;
    private final AtomicBoolean started = new AtomicBoolean(false);

    @Synchronized
    @PostConstruct
    public void init() {
        if (started.get()) {
            return;
        }
        final int subscriptionDelay = getSubscriptionDelay();
        if (subscriptionDelay <= 0) {
            return;
        }
        final int subscriptionInitialDelay = getSubscriptionInitialDelay();
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
