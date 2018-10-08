package com.cumulocity.microservice.actuatorconfiguration.annotation;

import com.cumulocity.microservice.actuatorconfiguration.configuration.ActuatorDefaultConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ActuatorDefaultConfiguration.class)
public @interface EnableActuatorDefaultConfiguration {
}
