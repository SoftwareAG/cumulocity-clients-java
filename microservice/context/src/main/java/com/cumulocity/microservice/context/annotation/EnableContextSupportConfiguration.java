package com.cumulocity.microservice.context.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.DeviceCredentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.scope.BaseScope;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

@Configuration
public class EnableContextSupportConfiguration {

    public static final String DEVICE_SCOPE = "device";
    public static final String TENANT_SCOPE = "tenant";

    @Bean
    public ContextService<MicroserviceCredentials> microserviceContextService() {
        return new ContextServiceImpl<>(MicroserviceCredentials.class);
    }

    @Bean
    public ContextService<DeviceCredentials> devicesContextService() {
        return new ContextServiceImpl<>(DeviceCredentials.class);
    }

//    @Bean
//    public <T> ContextService<T> contextService(InjectionPoint injectionPoint) {
//        final Class clazz = getClassParameter(injectionPoint);
//        return new ContextServiceImpl<T>(clazz);
//    }

    public static Class getClassParameter(InjectionPoint injectionPoint) {
        final Field field = injectionPoint.getField();
        if (field != null) {
            final ParameterizedType genericParameterType = (ParameterizedType) field.getGenericType();
            return (Class) genericParameterType.getActualTypeArguments()[0];
        }
        final MethodParameter methodParameter = injectionPoint.getMethodParameter();
        if (methodParameter != null) {
            final ParameterizedType genericParameterType = (ParameterizedType) methodParameter.getGenericParameterType();
            return (Class) genericParameterType.getActualTypeArguments()[0];
        }
        return null;
    }

    @Bean
    public CustomScopeConfigurer contextScopeConfigurer(
            final ContextService<MicroserviceCredentials> microserviceContextService,
            final ContextService<DeviceCredentials> deviceContextService) {
        final CustomScopeConfigurer configurer = new CustomScopeConfigurer();

        configurer.setScopes(ImmutableMap.<String, Object> builder()
                .put(DEVICE_SCOPE, new BaseScope(true) {
                    protected String getContextId() {
                        final DeviceCredentials context = deviceContextService.getContext();
                        return context.getTenant() + "/" + context.getUsername() + ":" + context.getPassword() + "," + context.getTfaToken();
                    }
                })
                .put(TENANT_SCOPE, new BaseScope(true) {
                    protected String getContextId() {
                        final MicroserviceCredentials context = microserviceContextService.getContext();
                        return context.getTenant();
                    }
                })
                .build()
        );

        return configurer;
    }

}
