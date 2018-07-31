package com.cumulocity.microservice.health.annotation;

import com.cumulocity.microservice.health.indicator.MemoryHealthIndicatorConfiguration;
import com.cumulocity.microservice.health.indicator.PlatformHealthIndicatorConfiguration;
import com.cumulocity.microservice.health.indicator.SubscriptionHealthIndicatorConfiguration;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 
 * Indicates that a /health endpoint is exposed to give microservice health status.
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@Import({
        PlatformHealthIndicatorConfiguration.class,
        MemoryHealthIndicatorConfiguration.class,
        SubscriptionHealthIndicatorConfiguration.class
})
public @interface EnableHealthIndicator {
}
