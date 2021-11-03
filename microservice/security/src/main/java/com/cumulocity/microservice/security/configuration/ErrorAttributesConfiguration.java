package com.cumulocity.microservice.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:microservice_error_attributes.properties", ignoreResourceNotFound = true)
public class ErrorAttributesConfiguration {
}
