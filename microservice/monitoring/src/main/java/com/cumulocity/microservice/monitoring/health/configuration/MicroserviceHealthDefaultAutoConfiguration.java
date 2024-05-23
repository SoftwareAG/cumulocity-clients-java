package com.cumulocity.microservice.monitoring.health.configuration;

import com.cumulocity.microservice.monitoring.health.annotation.EnableHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ConditionalOnBean(value = Object.class, annotation = EnableHealthIndicator.class)
@PropertySource(value = "classpath:microservice_health.properties", ignoreResourceNotFound = true)
public class MicroserviceHealthDefaultAutoConfiguration {
}
