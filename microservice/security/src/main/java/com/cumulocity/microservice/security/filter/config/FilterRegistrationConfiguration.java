package com.cumulocity.microservice.security.filter.config;

import com.cumulocity.microservice.security.filter.PostAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PreAuthenticateServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class FilterRegistrationConfiguration {

    /**
     * @param filter
     * @return A disabled filter registration bean, for Spring not to auto register the {@link PreAuthenticateServletFilter} filter which is mapped to /** including ignore paths.
     * The filter will be added manually under {@link com.cumulocity.microservice.security.annotation.EnableWebSecurityConfiguration#configure(HttpSecurity)}
     */
    @Bean
    public FilterRegistrationBean preAuthenticateFilterRegistration(PreAuthenticateServletFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        // Disable filter auto detection/registration by Spring
        registration.setEnabled(false);
        return registration;
    }

    /**
     * @param filter
     * @return A disabled filter registration bean, for Spring not to auto register the {@link PostAuthenticateServletFilter} filter which is mapped to /** including ignore paths.
     * The filter will be added manually under {@link com.cumulocity.microservice.security.annotation.EnableWebSecurityConfiguration#configure(HttpSecurity)}
     */
    @Bean
    public FilterRegistrationBean postAuthenticateFilterRegistration(PostAuthenticateServletFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        // Disable filter auto detection/registration by Spring
        registration.setEnabled(false);
        return registration;
    }
}
