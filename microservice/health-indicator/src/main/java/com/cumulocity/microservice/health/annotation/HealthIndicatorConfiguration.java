package com.cumulocity.microservice.health.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.health.indicator.PlatformHealthIndicator;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnClass({
        PlatformProperties.class,
        ContextService.class
})
@Import(PlatformHealthIndicator.class)
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
public class HealthIndicatorConfiguration {

    @Order(1001)
    @Configuration
    public class HealthSecurityConfiguration extends WebSecurityConfigurerAdapter {
        public void configure(WebSecurity webSecurity) {
            webSecurity.ignoring().antMatchers("/health","/prometheus");
        }
    }
}
