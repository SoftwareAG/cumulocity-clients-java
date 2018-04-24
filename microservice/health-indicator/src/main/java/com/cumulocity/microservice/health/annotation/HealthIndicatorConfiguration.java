package com.cumulocity.microservice.health.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.health.indicator.PlatformHealthIndicator;
import com.cumulocity.microservice.health.indicator.platform.PlatformHealthIndicatorProperties;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean({
        PlatformProperties.class,
        ContextService.class
})
@ComponentScan(basePackageClasses = PlatformHealthIndicator.class)
@EnableConfigurationProperties(PlatformHealthIndicatorProperties.class)
public class HealthIndicatorConfiguration {

}
