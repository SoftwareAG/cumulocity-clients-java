package com.cumulocity.microservice.monitoring.health.indicator.subscription;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("management.health.subscription")
public class SubscriptionHealthIndicatorProperties {

    public static class Details {
        private boolean enabled = true;
    }

}
