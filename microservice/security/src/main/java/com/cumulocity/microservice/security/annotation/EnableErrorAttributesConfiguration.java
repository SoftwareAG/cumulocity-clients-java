package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.configuration.ErrorAttributesConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ErrorAttributesConfiguration.class)
public @interface EnableErrorAttributesConfiguration {
}
