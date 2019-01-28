package com.cumulocity.microservice.settings.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Indicates the application can use microservice tenant options to override file properties and define new.
 * Not available in legacy mode.
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableTenantOptionSettingsConfiguration.class)
public @interface EnableTenantOptionSettings {
}
