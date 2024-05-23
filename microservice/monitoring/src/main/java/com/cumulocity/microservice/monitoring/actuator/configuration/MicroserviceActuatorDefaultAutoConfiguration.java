package com.cumulocity.microservice.monitoring.actuator.configuration;

import com.cumulocity.microservice.monitoring.actuator.annotation.EnableActuatorDefaultConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ConditionalOnBean(value = Object.class, annotation = EnableActuatorDefaultConfiguration.class)
@PropertySource(value = "classpath:microservice_actuator.properties", ignoreResourceNotFound = true)
public class MicroserviceActuatorDefaultAutoConfiguration {
}
