package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import java.util.Map;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.cumulocity.agent.server.servers.jaxrs.JaxrsServerConfiguration;

public class RestServerBuilder extends SpringServerBuilder<RestServerBuilder> {

    private final ServerBuilder builder;

    private final ResourceConfig resourceConfig = new ResourceConfig();

    protected RestServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public RestServerBuilder register(Class<?> componentClass) {
        resourceConfig.register(componentClass);
        return this;
    }

    public RestServerBuilder register(Class<?> componentClass, int bindingPriority) {
        resourceConfig.register(componentClass, bindingPriority);
        return this;
    }

    public RestServerBuilder register(Class<?> componentClass, Class<?>... contracts) {
        resourceConfig.register(componentClass, contracts);
        return this;
    }

    public RestServerBuilder register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        resourceConfig.register(componentClass, contracts);
        return this;
    }

    public RestServerBuilder register(Object component, int bindingPriority) {
        resourceConfig.register(component, bindingPriority);
        return this;
    }

    public RestServerBuilder register(Object component) {
        resourceConfig.register(component);
        return this;
    }

    public RestServerBuilder register(Object component, Class<?>... contracts) {
        resourceConfig.register(component, contracts);
        return this;
    }

    public RestServerBuilder register(Object component, Map<Class<?>, Integer> contracts) {
        resourceConfig.register(component, contracts);
        return this;
    }

    public Server build() {

        ConfigurableApplicationContext parentContext = builder.getContext();
        parentContext.getBeanFactory().registerSingleton("resourceConfiguration", resourceConfig);

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.setParent(parentContext);
        applicationContext.register(annotatedClasses(JaxrsServerConfiguration.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        applicationContext.refresh();
        applicationContext.start();
        return applicationContext.getBean(Server.class);
    }
}
