package com.cumulocity.microservice.api;

import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformParameters;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.analyzer.AbstractInjectionFailureAnalyzer;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;

/**
 * Detects missing {@link com.cumulocity.sdk.client.PlatformImpl PlatformImpl} or {@link PlatformParameters} beans
 * and displays instructions for alternative approach.
 */
@Order(1)
class NoSuchPlatformBeanDefinitionFailureAnalyzer extends AbstractInjectionFailureAnalyzer<NoSuchBeanDefinitionException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, NoSuchBeanDefinitionException cause, String description) {
        if (cause.getNumberOfBeansFound() != 0) {
            return null;
        }
        ResolvableType beanType = cause.getResolvableType();
        if (beanType == null) {
            return null;
        }
        Class<?> resolvedType = beanType.resolve();
        if (resolvedType == null) {
            return null;
        }
        if (PlatformParameters.class.isAssignableFrom(resolvedType)) {
            String desc = String.format("%s required a bean of type '%s' that could not be found.%n",
                    (description != null) ? description : "A component", resolvedType.getName());
            String action = String.format("Bean of this type is no longer available.%n" +
                    "Instead inject platform APIs directly " +
                    "or inject " + Platform.class.getName() + " and get the same platform APIs instances with getters.%n" +
                    "Use relative URL's starting with '/' with RestConnector bean to have them resolved automatically " +
                    "against the host defined in PlatformParameters#getHost().");
            return new FailureAnalysis(desc, action, cause);
        }
        return null;
    }
}
