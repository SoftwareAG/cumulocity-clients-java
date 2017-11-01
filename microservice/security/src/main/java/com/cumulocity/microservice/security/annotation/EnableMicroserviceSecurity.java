package com.cumulocity.microservice.security.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Requires UserDetailsService bean
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroserviceSecurityConfiguration.class)
public @interface EnableMicroserviceSecurity {
}
