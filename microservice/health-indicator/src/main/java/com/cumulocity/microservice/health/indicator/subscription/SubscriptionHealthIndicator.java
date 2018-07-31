package com.cumulocity.microservice.health.indicator.subscription;

import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "management.health.subscription.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class SubscriptionHealthIndicator extends AbstractHealthIndicator {

    private final SubscriptionHealthIndicatorProperties configuration;
    private final MicroserviceSubscriptionsService subscriptionsService;

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        if (subscriptionsService.isSubscribed()) {
            builder.up();
        } else {
            builder.down().withDetail("details", "Microservice subscription failed. Check logs.");
        }
        log.debug("health {}", builder.build());
    }

}
