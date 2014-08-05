package com.cumulocity.agent.server.protocol;

import java.io.IOException;

import org.glassfish.grizzly.*;
import org.glassfish.grizzly.filterchain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolFilter<T, K> extends BaseFilter {
    private static final Logger log = LoggerFactory.getLogger(ProtocolFilter.class);

    private final Transformer<Buffer, T> decoder;

    private final Transformer<K, Buffer> encoder;

    public ProtocolFilter(Transformer<Buffer, T> decoder, Transformer<K, Buffer> encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public NextAction handleConnect(FilterChainContext ctx) throws IOException {
        log.debug("new connection {}", ctx.getAddress());
        return super.handleConnect(ctx);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NextAction handleRead(FilterChainContext ctx) throws IOException {
        final Connection connection = ctx.getConnection();
        final Buffer message = (Buffer) ctx.getMessage();

        final TransformationResult<Buffer, T> result = decoder.transform(connection, message);

        switch (result.getStatus()) {
        case COMPLETE:
            final Buffer remainder = result.getExternalRemainder();
            final boolean hasRemaining = decoder.hasInputRemaining(connection, remainder);
            decoder.release(connection);
            ctx.setMessage(result.getMessage());
            if (hasRemaining) {
                return ctx.getInvokeAction(remainder);
            } else {
                return ctx.getInvokeAction();
            }
        case INCOMPLETE:
            return ctx.getStopAction(message);
        case ERROR:
            throw new TransformationException(getClass().getName() + " transformation error: (" + result.getErrorCode() + ") "
                    + result.getErrorDescription());
        }

        return ctx.getInvokeAction();
    }

    @Override
    @SuppressWarnings("unchecked")
    public NextAction handleWrite(FilterChainContext ctx) throws IOException {
        final Connection connection = ctx.getConnection();
        final K message = (K) ctx.getMessage();

        final TransformationResult<K, Buffer> result = encoder.transform(connection, message);

        switch (result.getStatus()) {
        case COMPLETE:
            ctx.setMessage(result.getMessage());
            final K remainder = result.getExternalRemainder();
            final boolean hasRemaining = encoder.hasInputRemaining(connection, remainder);
            encoder.release(connection);
            if (hasRemaining) {
                return ctx.getInvokeAction(remainder);
            } else {
                return ctx.getInvokeAction();
            }
        case INCOMPLETE:
            return ctx.getStopAction(message);
        case ERROR:
            throw new TransformationException(getClass().getName() + " transformation error: (" + result.getErrorCode() + ") "
                    + result.getErrorDescription());
        }

        return ctx.getInvokeAction();
    }

}
