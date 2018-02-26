package com.cumulocity.microservice.context.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.context.scope.BaseScope;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public CustomScopeConfigurer contextScopeConfigurer(
            final ContextService<MicroserviceCredentials> microserviceContextService,
            final ContextService<UserCredentials> userContextService) {
        final CustomScopeConfigurer configurer = new CustomScopeConfigurer();

//        todo implement scope clearing after SubscriptionRemovedEvent
        configurer.setScopes(ImmutableMap.<String, Object> builder()
                .put(USER_SCOPE, new BaseScope(true) {
                    protected String getContextId() {
                        final UserCredentials context = userContextService.getContext();
                        return context.getTenant() + "/" + context.getUsername() + ":" + context.getPassword() + "," + context.getTfaToken();
                    }
                })
                .put(TENANT_SCOPE, new BaseScope(true) {
                    protected String getContextId() {
                        final MicroserviceCredentials context = microserviceContextService.getContext();
                        return context.getTenant() + "/" + context.getUsername() + ":" + context.getPassword() + "," + context.getTfaToken();
                    }
                })
                .build()
        );

        return configurer;
    }

}
