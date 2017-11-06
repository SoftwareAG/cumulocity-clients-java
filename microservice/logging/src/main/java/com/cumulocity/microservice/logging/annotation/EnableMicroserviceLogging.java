package com.cumulocity.microservice.logging.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableMicroserviceLoggingConfiguration.class)
public @interface EnableMicroserviceLogging {
}
