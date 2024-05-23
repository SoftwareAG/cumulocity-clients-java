package com.cumulocity.microservice.monitoring.actuator.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableActuatorDefaultConfiguration {
}
