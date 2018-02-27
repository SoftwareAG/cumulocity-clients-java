package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.service.RoleService;
import com.cumulocity.microservice.security.service.SecurityExpressionService;
import com.cumulocity.microservice.security.service.SecurityUserDetailsService;
import com.cumulocity.microservice.security.service.impl.RoleServiceImpl;
import com.cumulocity.microservice.security.service.impl.SecurityExpressionServiceImpl;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Lazy
@Configuration
@ConditionalOnBean({
        PlatformParameters.class,
        EnableWebSecurityConfiguration.class,
})
@ConditionalOnMissingBean({UserDetailsService.class, RoleService.class})
public class UserDetailsServiceConfiguration {

    @Bean
    public UserDetailsService userDetailsService(@Qualifier("userPlatform") PlatformParameters userPlatform, RoleService roleService) {
        return new SecurityUserDetailsService(userPlatform, roleService);
    }

    @Bean
    public RoleService roleService(@Qualifier("userRestConnector") RestConnector restConnector) {
        return new RoleServiceImpl(restConnector);
    }

    @Bean
    public SecurityExpressionService securityExpressionService(ApplicationApi applicationApi) {
        return new SecurityExpressionServiceImpl(applicationApi);
    }
}
