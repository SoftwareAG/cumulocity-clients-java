package com.cumulocity.microservice.monitoring.health.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a /health endpoint is extended to give microservice health status.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableHealthIndicator {
}
