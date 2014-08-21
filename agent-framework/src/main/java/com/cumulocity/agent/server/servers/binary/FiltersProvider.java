package com.cumulocity.agent.server.servers.binary;

import java.util.List;

import org.glassfish.grizzly.filterchain.Filter;

public interface FiltersProvider {
    List<Filter> get();
}
