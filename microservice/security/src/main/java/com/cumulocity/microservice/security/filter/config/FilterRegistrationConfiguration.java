package com.cumulocity.microservice.security.filter.config;

import com.cumulocity.microservice.security.filter.PostAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PreAuthenticateServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegistrationConfiguration {

    @Bean
    public FilterRegistrationBean preAuthenticateFilterRegistration(PreAuthenticateServletFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        // Disable filter auto configured by Spring
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean postAuthenticateFilterRegistration(PostAuthenticateServletFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        // Disable filter auto configured by Spring
        registration.setEnabled(false);
        return registration;
    }
}
