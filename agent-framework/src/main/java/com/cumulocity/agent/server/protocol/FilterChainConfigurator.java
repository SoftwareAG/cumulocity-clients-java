package com.cumulocity.agent.server.protocol;

import java.util.List;

import org.glassfish.grizzly.filterchain.Filter;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.servers.BinaryServerConfigurator;

@Component
public class FilterChainConfigurator implements BinaryServerConfigurator {

    private final List<Filter> filters;

    @Autowired
    public FilterChainConfigurator(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public void configure(TCPNIOTransportBuilder server) {
        final FilterChainBuilder chain = FilterChainBuilder.stateless();
        chain.addAll(filters);
        server.setProcessor(chain.build());
    }

}
