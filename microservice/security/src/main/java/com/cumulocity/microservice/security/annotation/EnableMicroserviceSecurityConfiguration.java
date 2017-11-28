package com.cumulocity.microservice.security.annotation;


import com.cumulocity.microservice.security.filter.PostAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PreAuthenticateServletFilter;
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
@ComponentScan(basePackageClasses = PreAuthenticateServletFilter.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class EnableMicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PreAuthenticateServletFilter preAuthenticateServletFilter;

    @Autowired
    private PostAuthenticateServletFilter postAuthenticateServletFilter;

    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/metadata", "/health");
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    protected void configure(HttpSecurity http) throws Exception {
        final HttpSecurity security = http.authorizeRequests().anyRequest().

                fullyAuthenticated().and().

                httpBasic().and().

                csrf().disable();

        security.addFilterBefore(preAuthenticateServletFilter, BasicAuthenticationFilter.class);
        security.addFilterAfter(postAuthenticateServletFilter, BasicAuthenticationFilter.class);

    }
}

