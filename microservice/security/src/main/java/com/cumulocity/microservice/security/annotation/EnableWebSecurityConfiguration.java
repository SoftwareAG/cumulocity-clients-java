package com.cumulocity.microservice.security.annotation;


import com.cumulocity.microservice.security.filter.PostAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PreAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.config.FilterRegistrationConfiguration;
import com.cumulocity.microservice.security.token.CumulocityOAuthMicroserviceFilter;
import com.cumulocity.microservice.security.token.JwtAuthenticatedTokenCache;
import com.cumulocity.microservice.security.token.JwtTokenAuthenticationGuavaCache;
import com.cumulocity.microservice.security.token.JwtTokenAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@Order(99)
@EnableWebSecurity
@ComponentScan(
        value = "com.cumulocity.microservice.security.token",
        basePackageClasses = {
                FilterRegistrationConfiguration.class,
                PreAuthenticateServletFilter.class
        })
public class EnableWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PreAuthenticateServletFilter preAuthenticateServletFilter;

    @Autowired
    private PostAuthenticateServletFilter postAuthenticateServletFilter;

    @Autowired
    private CumulocityOAuthMicroserviceFilter cumulocityOAuthMicroserviceFilter;

    @Autowired
    private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    @Autowired(required = false)
    private JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        setCustomJwtAuthenticatedCacheIfExists();
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(jwtTokenAuthenticationProvider);
    }

    private void setCustomJwtAuthenticatedCacheIfExists() {
        if (jwtAuthenticatedTokenCache != null) {
            log.info("Custom implementation for token cache is used");
            jwtTokenAuthenticationProvider.setTokenCache(jwtAuthenticatedTokenCache);
        } else {
            log.info("Default Guava implementation for token cache is used");
            jwtTokenAuthenticationProvider.setTokenCache(new JwtTokenAuthenticationGuavaCache());
        }
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/metadata", "/health", "/prometheus", "/metrics");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final HttpSecurity security = http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .requestCache().disable();

        http.addFilterBefore(cumulocityOAuthMicroserviceFilter, BasicAuthenticationFilter.class);

        security.addFilterBefore(preAuthenticateServletFilter, BasicAuthenticationFilter.class);
        security.addFilterAfter(postAuthenticateServletFilter, BasicAuthenticationFilter.class);

    }
}

