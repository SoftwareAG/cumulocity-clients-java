package com.cumulocity.microservice.api;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Prepares environment to work with platform api. Injects basic beans for communication with the platform.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroservicePlatformApiConfiguration.class)
public @interface EnableMicroservicePlatformApi {
}
