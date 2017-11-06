package com.cumulocity.microservice.subscription.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * Requires application name. Emits MicroserviceSubscription*Event events.
 * Additional configuration is provided by ObjectMapper and MicroserviceMetadataRepresentation
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroserviceSubscriptionConfiguration.class)
public @interface EnableMicroserviceSubscription {
}
