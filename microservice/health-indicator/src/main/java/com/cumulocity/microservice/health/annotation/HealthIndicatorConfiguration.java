package com.cumulocity.microservice.health.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.health.indicator.PlatformHealthIndicator;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnClass({
        PlatformProperties.class,
        ContextService.class
})
@Import(PlatformHealthIndicator.class)
public class HealthIndicatorConfiguration extends WebSecurityConfigurerAdapter {
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/health");
    }
}
