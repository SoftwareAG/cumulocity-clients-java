package com.cumulocity.microservice.monitoring.actuator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:microservice_actuator.properties", ignoreResourceNotFound = true)
public class ActuatorDefaultConfiguration {
}
