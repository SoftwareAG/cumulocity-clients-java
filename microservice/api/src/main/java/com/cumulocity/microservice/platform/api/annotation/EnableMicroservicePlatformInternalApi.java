package com.cumulocity.microservice.platform.api.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 
 * Indicates that usage of internal Cumulocity APIs are enabled.
 * Internal Cumulocity API requests do not increase the request counts unlike regular Cumulocity API requests.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroservicePlatformInternalApiConfiguration.class)
public @interface EnableMicroservicePlatformInternalApi {
}
