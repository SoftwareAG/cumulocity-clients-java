package com.cumulocity.microservice.context.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableContextSupportConfiguration.class)
public @interface EnableContextSupport {
}
