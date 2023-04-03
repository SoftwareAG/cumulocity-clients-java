package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.context.request.RequestContextListener;

@Configuration(proxyBeanMethods = false)
public class CumulocityOAuthConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OAuthPostAuthorizationContextProvider oAuthPostAuthorizationContextProvider(
            ObjectProvider<MicroserviceSubscriptionsService> subscriptionsService) {
        String applicationName = environment.getProperty("application.name", "");
        OAuthPostAuthorizationContextProvider provider = new OAuthPostAuthorizationContextProvider(applicationName);
        subscriptionsService.ifAvailable(provider::setSubscriptionsService);
        return provider;
    }

    @Bean
    public JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider(
            JwtAuthenticatedTokenCache jwtAuthenticatedTokenCache ) {
        return new JwtTokenAuthenticationProvider(environment, jwtAuthenticatedTokenCache);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Configuration(proxyBeanMethods = false)
    @EnableGlobalAuthentication
    static class AuthenticationConfigurer {

        @Autowired
        private JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

        @Autowired
        public void configureAuthenticationManager(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(jwtTokenAuthenticationProvider);
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class FilterConfigurer {

        @Autowired
        private AuthenticationConfiguration authenticationConfiguration;
        @Autowired(required = false)
        private AuthenticationEntryPoint authenticationEntryPoint;
        @Autowired
        private ContextService<UserCredentials> userContextService;

        @Bean
        public CumulocityOAuthMicroserviceFilter cumulocityOAuthMicroserviceFilter()
                throws Exception {
            return new CumulocityOAuthMicroserviceFilter(authenticationConfiguration.getAuthenticationManager(),
                    authenticationEntryPoint, userContextService);
        }

        /**
         * Disables auto-registration of {@link CumulocityOAuthMicroserviceFilter} as it is mean for use in {@link org.springframework.security.web.SecurityFilterChain SecurityFilterChain}.
         * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.webserver.add-servlet-filter-listener.spring-bean.disable
         */
        @Bean
        public FilterRegistrationBean<CumulocityOAuthMicroserviceFilter> cumulocityOAuthMicroserviceFilterRegistration(CumulocityOAuthMicroserviceFilter filter) {
            FilterRegistrationBean<CumulocityOAuthMicroserviceFilter> registration = new FilterRegistrationBean<>(filter);
            // Disable filter auto detection/registration by Spring
            registration.setEnabled(false);
            return registration;
        }
    }
}
