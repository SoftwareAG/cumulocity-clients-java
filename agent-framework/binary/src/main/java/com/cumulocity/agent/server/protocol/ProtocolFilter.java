package com.cumulocity.agent.server.protocol;

import java.io.IOException;

import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.TransformationException;
import org.glassfish.grizzly.TransformationResult;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolFilter<T, K> extends BaseFilter {
    private static final Logger log = LoggerFactory.getLogger(ProtocolFilter.class);

    private final Transformer<Buffer, T> decoder;

    private final Transformer<K, Buffer> encoder;

    private final ProtocolExceptionHandler exceptionHandler;

    public ProtocolFilter(Transformer<Buffer, T> decoder, Transformer<K, Buffer> encoder, ProtocolExceptionHandler exceptionHandler) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public NextAction handleConnect(FilterChainContext ctx) throws IOException {
        log.debug("new connection {}", ctx.getAddress());
        return super.handleConnect(ctx);
    }

    @Override
    @SuppressWarnings("unchecked")
    public NextAction handleRead(FilterChainContext ctx) throws IOException {
        return handle(decoder, ctx, new ExceptionCallback() {
            @Override
            public NextAction call(FilterChainContext ctx, Throwable cause) {
                return exceptionHandler.onReadException(ctx, cause);
            }
        });
    }

    private <I, O> NextAction handle(Transformer<I, O> transformer, FilterChainContext ctx, ExceptionCallback callback) {
        final Connection connection = ctx.getConnection();
        final I message = (I) ctx.getMessage();
        TransformationResult<I, O> result = null;
        try {
            result = transformer.transform(connection, message);
            switch (result.getStatus()) {
            case COMPLETE:
                final I remainder = result.getExternalRemainder();
                final boolean hasRemaining = transformer.hasInputRemaining(connection, remainder);
                transformer.release(connection);
                ctx.setMessage(result.getMessage());
                if (hasRemaining) {
                    return ctx.getInvokeAction(remainder);
                } else {
                    return ctx.getInvokeAction();
                }
            case INCOMPLETE:
                return ctx.getStopAction(message);
            case ERROR:
                return callback.call(ctx,
                        new TransformationException(getClass().getName() + " transformation error: (" + result.getErrorCode() + ") "
                                + result.getErrorDescription()));
            }
        } catch (Throwable ex) {
            return callback.call(ctx, ex);
        } finally {
            if (result != null) {
                result.recycle();
            }
        }

        return ctx.getInvokeAction();
    }

    @Override
    @SuppressWarnings("unchecked")
    public NextAction handleWrite(FilterChainContext ctx) throws IOException {
        return handle(encoder, ctx, new ExceptionCallback() {
            @Override
            public NextAction call(FilterChainContext ctx, Throwable cause) {
                return exceptionHandler.onWriteException(ctx, cause);
            }
        });
    }

    interface ExceptionCallback {
        NextAction call(FilterChainContext ctx, Throwable cause);
    }
}
