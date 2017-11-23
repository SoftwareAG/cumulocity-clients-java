package com.cumulocity.microservice.agent.server.security.conf;

import com.cumulocity.agent.server.context.ServletDeviceContextFilter;
import com.cumulocity.agent.server.feature.ContextFeature;
import com.cumulocity.agent.server.feature.DeviceAuthorizationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Order(99)
@ComponentScan("com.cumulocity.microservice.agent.server.security")
@Configuration
@Import({
    ContextFeature.class,
    DeviceAuthorizationFeature.class,
    SecurityAutoConfiguration.class,
    EmbeddedFilterRegistrationBeanConfiguration.class
})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    private ServletDeviceContextFilter deviceContextFilter;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/metadata", "/health");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().

        fullyAuthenticated().and().

        httpBasic().and().

        csrf().disable();

        http.addFilterBefore(deviceContextFilter, BasicAuthenticationFilter.class);
    }
}
