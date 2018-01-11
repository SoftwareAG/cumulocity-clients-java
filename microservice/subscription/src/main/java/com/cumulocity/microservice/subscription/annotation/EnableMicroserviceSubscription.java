package com.cumulocity.microservice.subscription.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 
 * Indicates the application can use microservice subscription interface and emits MicroserviceSubscription*Event events.
 * Additional configuration is provided by ObjectMapper and MicroserviceMetadataRepresentation. 
 *
**/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroserviceSubscriptionConfiguration.class)
public @interface EnableMicroserviceSubscription {
}
