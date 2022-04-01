package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.annotation.EnableContextSupport;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.PlatformParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class NoSuchPlatformBeanDefinitionFailureAnalyzerTest {

    NoSuchPlatformBeanDefinitionFailureAnalyzer analyzer = new NoSuchPlatformBeanDefinitionFailureAnalyzer();

    @Test
    void shouldAnalyzeMissingPlatformImpl() {
        FailureAnalysis analysis = analyzer.analyze(createFailure(MissingPlatformImplContext.class));
        assertThat(analysis.getDescription())
                .contains("required a bean of type '" + PlatformImpl.class.getName() + "' that could not be found");
    }

    @Test
    void shouldAnalyzeMissingPlatformParameters() {
        FailureAnalysis analysis = analyzer.analyze(createFailure(MissingPlatformParametersContext.class));
        assertThat(analysis.getDescription())
                .contains("required a bean of type '" + PlatformParameters.class.getName() + "' that could not be found");
    }

    private BeanCreationException createFailure(Class<?> configClass) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(configClass);
            try {
                context.refresh();
            } catch (BeanCreationException e) {
                return e;
            }
        }
        return null;
    }

    @Configuration(proxyBeanMethods = false)
    @EnableContextSupport
    @EnableMicroservicePlatformApi
    static class MissingPlatformImplContext {
        @Autowired PlatformImpl platform;
    }

    @Configuration(proxyBeanMethods = false)
    @EnableContextSupport
    @EnableMicroservicePlatformApi
    static class MissingPlatformParametersContext {
        @Autowired PlatformParameters parameters;
    }
}
