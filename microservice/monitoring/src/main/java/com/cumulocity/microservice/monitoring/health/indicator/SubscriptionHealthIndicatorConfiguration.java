package com.cumulocity.microservice.monitoring.health.indicator;

import com.cumulocity.microservice.monitoring.health.indicator.subscription.SubscriptionHealthIndicator;
import com.cumulocity.microservice.monitoring.health.indicator.subscription.SubscriptionHealthIndicatorProperties;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "management.health.subscription.enabled", havingValue = "true")
@EnableConfigurationProperties(SubscriptionHealthIndicatorProperties.class)
public class SubscriptionHealthIndicatorConfiguration {

    @Bean
    @ConditionalOnBean(MicroserviceSubscriptionsService.class)
    public SubscriptionHealthIndicator subscriptionHealthIndicator(
            MicroserviceSubscriptionsService subscriptionsService,
            SubscriptionHealthIndicatorProperties configuration) {
        return new SubscriptionHealthIndicator(configuration, subscriptionsService);
    }

}
