package com.cumulocity.microservice.platform.api.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *  Prepares environment to work with platform api. Injects basic beans for comuunication with the platform.
 *
 *  @deprecated Use @EnableMicroservicePlatformApi
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroservicePlatformInternalApiConfiguration.class)
@Deprecated
public @interface EnableMicroservicePlatformInternalApi {
}
