package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import org.glassfish.grizzly.Grizzly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cumulocity.agent.server.servers.binary.BinaryServerConfiguration;

public class BinaryServerBuilder extends SpringServerBuilder<BinaryServerBuilder> {

    private static final Logger log = LoggerFactory.getLogger(BinaryServerBuilder.class);

    private final ServerBuilder builder;

    public BinaryServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public Server build() {
        Grizzly.getDotedVersion();
        final AnnotationConfigApplicationContext parentContext = builder.getContext();

        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.setParent(parentContext);

        applicationContext.register(annotatedClasses(BinaryServerConfiguration.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        applicationContext.refresh();
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
                    log.error(e.getMessage(), e);
                    stop();
                }

            }

            @Override
            public void awaitTerminated() {
                server.awaitTerminated();

            }
        };
    }
}
