package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.service.RoleService;
import com.cumulocity.microservice.security.service.SecurityUserDetailsService;
import com.cumulocity.microservice.security.service.impl.RoleServiceImpl;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
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
        EnableMicroserviceSecurityConfiguration.class,
})
@ConditionalOnMissingBean({UserDetailsService.class, RoleService.class})
public class UserDetailsServiceConfiguration {

    @Bean
    public UserDetailsService userDetailsService(PlatformParameters platformParameters, RoleService roleService) {
        return new SecurityUserDetailsService(platformParameters, roleService);
    }

    @Bean
    public RoleService roleService(PlatformParameters platformParameters, RestConnector restConnector) {
        return new RoleServiceImpl(platformParameters, restConnector);
    }
}
