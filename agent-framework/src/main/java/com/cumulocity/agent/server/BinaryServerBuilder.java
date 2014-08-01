package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cumulocity.agent.server.config.BinnaryServerConfiguration;

public class BinaryServerBuilder extends SpringServerBuilder<BinaryServerBuilder> {

    private final ServerBuilder builder;

    protected BinaryServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }
    
    public Server build() {
        
        ConfigurableApplicationContext parentContext = builder.getContext();

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.setParent(parentContext);
        
        applicationContext.register(annotatedClasses(BinnaryServerConfiguration.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        applicationContext.refresh();
        return applicationContext.getBean(Server.class);
    }
    
   
}
