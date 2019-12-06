package com.cumulocity.microservice.monitoring.health.annotation;

import com.cumulocity.microservice.monitoring.health.indicator.MemoryHealthIndicatorConfiguration;
import com.cumulocity.microservice.monitoring.health.indicator.PlatformHealthIndicatorConfiguration;
import com.cumulocity.microservice.monitoring.health.indicator.SubscriptionHealthIndicatorConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.lang.annotation.*;

/**
 * 
 * Indicates that a /actuator/health endpoint is exposed to give microservice health status.
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PropertySource(value = "classpath:microservice_health.properties", ignoreResourceNotFound = true)
@Import({
        PlatformHealthIndicatorConfiguration.class,
        MemoryHealthIndicatorConfiguration.class,
        SubscriptionHealthIndicatorConfiguration.class
})
public @interface EnableHealthIndicator {
}
