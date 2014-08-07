package com.cumulocity.agent.server.protocol;

import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;

public interface ProtocolExceptionHandler {

    NextAction onReadException(FilterChainContext ctx, Throwable cause);

    NextAction onWriteException(FilterChainContext ctx, Throwable cause);

}
