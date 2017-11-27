package com.cumulocity.microservice.agent.server.security.conf;

import com.cumulocity.agent.server.context.ServletDeviceContextFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @deprecated it is based on old version of spring-boot - needs to be upgraded
 */
@Deprecated
@Configuration
@ConditionalOnClass(FilterRegistrationBean.class)
public class EmbeddedFilterRegistrationBeanConfiguration {
    // filter configured by DeviceAuthorizationFeature must be first one in order to use agent framework in UserDetailsService
    @Bean
    public FilterRegistrationBean deviceContextFilterChain(ServletDeviceContextFilter deviceContextFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(deviceContextFilter);
        registration.setOrder(0);
        registration.setName("ServletDeviceContextFilter");
        return registration;
    }
}
