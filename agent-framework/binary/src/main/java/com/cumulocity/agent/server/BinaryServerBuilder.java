package com.cumulocity.agent.server;

import org.glassfish.grizzly.Grizzly;
import org.springframework.context.ConfigurableApplicationContext;

import com.cumulocity.agent.server.servers.binary.BinaryServerConfiguration;

public class BinaryServerBuilder extends SpringServerBuilder<BinaryServerBuilder> {

    private final ServerBuilder builder;

    public BinaryServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public ConfigurableApplicationContext run(String... args) {
        Grizzly.getDotedVersion();
        //@formatter:off
        return builder.context()
        .sources(annotatedClasses(BinaryServerConfiguration.class))
        .run(args);
        //@formatter:on
    }
}
