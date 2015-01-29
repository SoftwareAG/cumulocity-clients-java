package com.cumulocity.agent.server.devicecontrol;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

public class ConcurrentOperationDispatcher implements OperationDispatcher {

    private final static Logger log = LoggerFactory.getLogger(ConcurrentOperationDispatcher.class);

    private OperationExecutor handler;

    private Executor executor;

    private DeviceContext context;

    private DeviceContextService contextService;

    public ConcurrentOperationDispatcher(OperationExecutor input, Executor executor, DeviceContext context,
            DeviceContextService contextService) {
        this.handler = input;
        this.executor = executor;
        this.context = context;
        this.contextService = contextService;
    }

    @Override
    public boolean supports(OperationRepresentation operation) {
        return handler.supports(operation);
    }

    @Override
    public void dispatch(final OperationRepresentation operation) {
        executor.execute(contextService.withinContext(context, new Runnable() {
            @Override
            public void run() {
                try {
                    handler.handle(operation);
                } catch (Exception ex) {
                    log.error("handle operation failed {}", operation, ex);
                }
            }
        }));
    }
}
