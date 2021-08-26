package com.cumulocity.microservice.context.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.context.scope.BaseScope;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import static com.cumulocity.microservice.context.scope.BaseScope.DEFAULT_CACHE_EXPIRATION_TIMEOUT;

@Configuration
public class EnableContextSupportConfiguration {

    public static final String USER_SCOPE = "user";
    public static final String TENANT_SCOPE = "tenant";

    @Bean
    public ContextService<Credentials> credentialsContextService() {
        return new ContextServiceImpl<>(Credentials.class);
    }

    @Bean
    public ContextService<MicroserviceCredentials> microserviceContextService() {
        return new ContextServiceImpl<>(MicroserviceCredentials.class);
    }

    @Bean
    public ContextService<UserCredentials> userContextService() {
        return new ContextServiceImpl<>(UserCredentials.class);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CustomScopeConfigurer contextScopeConfigurer(
            final ContextService<MicroserviceCredentials> microserviceContextService,
            final ContextService<UserCredentials> userContextService,
            @Value("${tenantCacheExpirationTimeout:" + DEFAULT_CACHE_EXPIRATION_TIMEOUT + "}") Long cacheExpirationTimeout) {
        final CustomScopeConfigurer configurer = new CustomScopeConfigurer();

//        todo implement scope clearing after SubscriptionRemovedEvent
        configurer.setScopes(ImmutableMap.<String, Object>builder()
                .put(USER_SCOPE, new BaseScope(true) {
                    protected String getContextId() {
                        final UserCredentials context = userContextService.getContext();
                        return context.getTenant() + "/" + context.getUsername() + ":" + context.getPassword() + "," +
                                context.getAppKey() + "," +
                                context.getOAuthAccessToken() + ":" + context.getXsrfToken() + "," +
                                context.getTfaToken();
                    }
                })
                .put(TENANT_SCOPE, new BaseScope(true, cacheExpirationTimeout) {
                    protected String getContextId() {
                        final MicroserviceCredentials context = microserviceContextService.getContext();
                        return context.getTenant() + "/" + context.getUsername() + ":" + context.getPassword() + "," +
                                context.getAppKey() + "," +
                                context.getOAuthAccessToken() + ":" + context.getXsrfToken() + "," +
                                context.getTfaToken();
                    }
                })
                .build()
        );

        return configurer;
    }

}
