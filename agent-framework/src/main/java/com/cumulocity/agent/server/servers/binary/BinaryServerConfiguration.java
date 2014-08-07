package com.cumulocity.agent.server.servers.binary;

import static org.glassfish.grizzly.filterchain.FilterChainBuilder.stateless;

import java.util.List;

import org.glassfish.grizzly.filterchain.Filter;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulocity.agent.server.config.CommonConfiguration;

@Configuration
@ComponentScan(basePackages = "com.cumulocity.agent.server.protocol")
@Import(CommonConfiguration.class)
public class BinaryServerConfiguration {

    @Bean
    @Autowired
    public BinaryServer server(List<BinaryServerConfigurator> configurators) {
        return new BinaryServer(configurators);
    }

    @Bean
    @Autowired
    public FilterChainBuilder chain(FiltersProvider filters) {
        return stateless().addFirst(new TransportFilter()).addAll(filters.get());
    }
}
