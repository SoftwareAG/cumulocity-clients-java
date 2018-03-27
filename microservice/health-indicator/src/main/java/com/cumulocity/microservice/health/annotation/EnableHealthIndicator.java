package com.cumulocity.microservice.health.annotation;

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
@Import(HealthIndicatorConfiguration.class)
public @interface EnableHealthIndicator {
}
