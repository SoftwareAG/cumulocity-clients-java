package com.cumulocity.microservice.autoconfigure;

import com.cumulocity.microservice.logging.annotation.EnableMicroserviceLogging;
import com.cumulocity.microservice.platform.api.annotation.EnableMicroservicePlatformInternalApi;
import com.cumulocity.microservice.security.annotation.EnableMicroserviceSecurity;
import com.cumulocity.microservice.subscription.annotation.EnableMicroserviceSubscription;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication
@EnableMicroserviceLogging
@EnableMicroserviceSecurity
@EnableMicroserviceSubscription
@EnableMicroservicePlatformInternalApi
public @interface MicroserviceApplication {
}
