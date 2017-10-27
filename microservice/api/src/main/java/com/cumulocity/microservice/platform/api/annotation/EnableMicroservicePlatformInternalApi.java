package com.cumulocity.microservice.platform.api.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroservicePlatformInternalApiConfiguration.class)
public @interface EnableMicroservicePlatformInternalApi {
}
