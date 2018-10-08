package com.cumulocity.microservice.monitoring.actuator.annotation;

import com.cumulocity.microservice.monitoring.actuator.configuration.ActuatorDefaultConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ActuatorDefaultConfiguration.class)
public @interface EnableActuatorDefaultConfiguration {
}
