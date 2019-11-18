package com.cumulocity.microservice.security.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Indicates that all the exposed endpoints other than /metadata requires 
 * base64 authorization and authentication.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        EnableWebSecurityConfiguration.class,
        EnableGlobalMethodSecurityConfiguration.class,
        TokenCacheConfiguration.class
})
public @interface EnableMicroserviceSecurity {
}
