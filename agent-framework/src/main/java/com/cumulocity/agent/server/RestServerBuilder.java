package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.cumulocity.agent.server.servers.jaxrs.JaxrsServerConfiguration;

public class RestServerBuilder extends SpringServerBuilder<RestServerBuilder> {


    private final ServerBuilder builder;

    protected RestServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public Server build() {
    	
        ConfigurableApplicationContext parentContext = builder.getContext();
        parentContext.getBeanFactory().registerSingleton("resourceConfiguration", new ResourceConfig() {
            {
                for (Class<?> component : components) {
                    register(component);
                }
                packages(packages.toArray(new String[packages.size()]));
            }
        });

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

        applicationContext.setParent(parentContext);
        applicationContext.register(annotatedClasses(JaxrsServerConfiguration.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        applicationContext.refresh();
        return applicationContext.getBean(Server.class);
    }


}
