package com.cumulocity.agent.server.feature;

import static org.glassfish.grizzly.filterchain.FilterChainBuilder.stateless;

import java.util.List;

import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.Server;
import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.config.ServerLicecycle;
import com.cumulocity.agent.server.servers.binary.BinaryServerRunner;
import com.cumulocity.agent.server.servers.binary.BinaryServerConfigurator;
import com.cumulocity.agent.server.servers.binary.FiltersProvider;

@Configuration
@ComponentScan(basePackages = "com.cumulocity.agent.server.protocol")
@Import({ CommonConfiguration.class, ServerLicecycle.class })
public class BinaryServer {
    static {
        Grizzly.getDotedVersion();
    }

    @Bean
    @Autowired
    public Server server(List<BinaryServerConfigurator> configurators) {
        return new BinaryServerRunner(configurators);
    }

    @Bean
    @Autowired
    public FilterChainBuilder chain(FiltersProvider filters) {
        return stateless().addFirst(new TransportFilter()).addAll(filters.get());
    }
}
