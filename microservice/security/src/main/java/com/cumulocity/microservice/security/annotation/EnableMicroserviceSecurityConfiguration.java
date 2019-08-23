package com.cumulocity.microservice.security.annotation;


import com.cumulocity.agent.server.context.ServletDeviceContextFilter;
import com.cumulocity.microservice.security.token.CumulocityOAuthMicroserviceFilter;
import com.cumulocity.microservice.security.token.JwtTokenAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@Order(99)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
@ComponentScan("com.cumulocity.microservice.security.token")
public class EnableMicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    private ServletDeviceContextFilter deviceContextFilter;

    @Autowired
    private CumulocityOAuthMicroserviceFilter cumulocityOAuthMicroserviceFilter;

    @Autowired
    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/metadata", "/health");
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(jwtTokenAuthenticationProvider);
    }

    protected void configure(HttpSecurity http) throws Exception {
        HttpSecurity disable = http.authorizeRequests().anyRequest().

                fullyAuthenticated().and().

                httpBasic().and().

                csrf().disable().
                securityContext().disable().
                sessionManagement().disable().
                requestCache().disable();
        http.addFilterBefore(cumulocityOAuthMicroserviceFilter, BasicAuthenticationFilter.class);
//        in microservice sdk we won't have device-context-filter (we will have microservice equivalent)
        if (deviceContextFilter != null) {
            disable.addFilterBefore(deviceContextFilter, BasicAuthenticationFilter.class);
        }
    }
}

