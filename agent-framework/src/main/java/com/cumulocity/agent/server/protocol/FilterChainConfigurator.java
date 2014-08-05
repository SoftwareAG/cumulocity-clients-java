package com.cumulocity.agent.server.protocol;

import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.servers.binary.BinaryServerConfigurator;

@Component
public class FilterChainConfigurator implements BinaryServerConfigurator {


    private final FilterChainBuilder filters;

    @Autowired
    public FilterChainConfigurator(FilterChainBuilder filters) {
        this.filters = filters;
    }

    @Override
    public void configure(TCPNIOTransportBuilder server) {
        server.setProcessor(filters.build());
    }

}
