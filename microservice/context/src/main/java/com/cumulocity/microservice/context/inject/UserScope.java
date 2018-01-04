package com.cumulocity.microservice.context.inject;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

import static com.cumulocity.microservice.context.annotation.EnableContextSupportConfiguration.USER_SCOPE;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = USER_SCOPE, proxyMode = TARGET_CLASS)
public @interface UserScope {
    String value() default "";
}
