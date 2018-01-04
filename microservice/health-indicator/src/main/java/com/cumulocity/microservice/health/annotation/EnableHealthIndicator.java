package com.cumulocity.microservice.health.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HealthIndicatorConfiguration.class)
public @interface EnableHealthIndicator {
}
