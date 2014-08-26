package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cumulocity.agent.server.servers.binary.BinaryServerConfiguration;

public class BinaryServerBuilder extends SpringServerBuilder<BinaryServerBuilder> {

    private final ServerBuilder builder;

    protected BinaryServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public Server build() {

        final AnnotationConfigApplicationContext parentContext = builder.getContext();

        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.setParent(parentContext);

        applicationContext.register(annotatedClasses(BinaryServerConfiguration.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        
        applicationContext.refresh();
        applicationContext.start();
        final Server server = applicationContext.getBean(Server.class);
        return new Server() {

            @Override
            public void stop() {
                applicationContext.stop();
                applicationContext.close();
                parentContext.close();
                server.stop();
            }

            @Override
            public void start() {
                try {
                    server.start();
                } catch (Exception e) {
                    stop();
                }

            }
        };
    }

}
