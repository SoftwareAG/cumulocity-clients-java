package com.cumulocity.microservice.security.annotation;

import com.cumulocity.microservice.security.filter.PostAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PreAuthenticateServletFilter;
import com.cumulocity.microservice.security.filter.PrePostFiltersConfiguration;
import com.cumulocity.microservice.security.token.CumulocityOAuthConfiguration;
import com.cumulocity.microservice.security.token.CumulocityOAuthMicroserviceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Order(99)
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@Import({
        CumulocityOAuthConfiguration.class,
        PrePostFiltersConfiguration.class
})
public class EnableWebSecurityConfiguration {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public static WebSecurityCustomizer allowObservability() {
        return web -> web.ignoring()
                .antMatchers("/metadata", "/health", "/prometheus", "/metrics");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CumulocityOAuthMicroserviceFilter cumulocityOAuthMicroserviceFilter,
                                                   PreAuthenticateServletFilter preAuthenticateServletFilter,
                                                   PostAuthenticateServletFilter postAuthenticateServletFilter)
            throws Exception {

        http
                .authorizeRequests(authorize -> authorize.anyRequest().fullyAuthenticated())
                .httpBasic(withDefaults())
                .csrf().disable()
                .securityContext().disable()
                .sessionManagement().disable()
                .requestCache().disable();

        http.addFilterBefore(cumulocityOAuthMicroserviceFilter, BasicAuthenticationFilter.class);

        http.addFilterBefore(preAuthenticateServletFilter, BasicAuthenticationFilter.class);
        http.addFilterAfter(postAuthenticateServletFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }
}
