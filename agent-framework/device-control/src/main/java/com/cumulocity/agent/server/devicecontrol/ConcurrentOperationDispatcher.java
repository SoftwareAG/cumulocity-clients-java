package com.cumulocity.agent.server.devicecontrol;

import static com.cumulocity.rest.representation.operation.Operations.asFailedOperation;
import static com.google.common.base.Throwables.getRootCause;
import static java.lang.String.format;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

public class ConcurrentOperationDispatcher implements OperationDispatcher {

    private final static Logger log = LoggerFactory.getLogger(ConcurrentOperationDispatcher.class);

    private final OperationExecutor handler;

    private final Executor executor;

    private final DeviceContext context;

    private final DeviceContextService contextService;

    private final DeviceControlRepository deviceControl;

    public ConcurrentOperationDispatcher(OperationExecutor input, Executor executor, DeviceContext context,
            DeviceContextService contextService, DeviceControlRepository deviceControl) {
        this.handler = input;
        this.executor = executor;
        this.context = context;
        this.contextService = contextService;
        this.deviceControl = deviceControl;
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
                    final Throwable rootCause = getRootCause(ex);
                    deviceControl.save(asFailedOperation(operation.getId(),
                            format("Opertion dispatch failed %s : %s", rootCause.getClass().getName(), rootCause.getMessage())));
                }
            }
        }));
    }
}
