package com.cumulocity.agent.server;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.FluentIterable.from;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.cumulocity.agent.server.servers.mvc.MvcServerConfiguration;

public class MvcServerBuilder extends SpringServerBuilder<MvcServerBuilder> {

    private static final Logger log = LoggerFactory.getLogger(MvcServerBuilder.class);

    private final ServerBuilder builder;

    protected MvcServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public Server build() {
        try {
            ConfigurableApplicationContext parentContext = builder.getContext();

            AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();

            applicationContext.setParent(parentContext);
            applicationContext.register(annotatedClasses(MvcServerConfiguration.class));
            if (!packages.isEmpty()) {
                applicationContext.scan(from(packages).toArray(String.class));
            }
            applicationContext.refresh();
            applicationContext.start();
            return applicationContext.getBean(Server.class);
        } catch (Exception ex) {
            log.error("build server failed", ex);
            throw propagate(ex);
        }
    }
}
