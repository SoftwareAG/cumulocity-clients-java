package com.cumulocity.agent.server.devicecontrol;

import static com.cumulocity.model.operation.OperationStatus.*;
import static com.cumulocity.rest.representation.operation.Operations.asFailedOperation;
import static com.google.common.base.Throwables.getRootCause;
import static com.google.common.collect.Queues.newConcurrentLinkedQueue;
import static java.lang.String.format;

import java.util.Queue;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;
import com.google.common.base.Throwables;

public class SynchronizedOperationDispatcher implements OperationDispatcher {

    private static final Logger log = LoggerFactory.getLogger(SynchronizedOperationDispatcher.class);

    private final OperationExecutor handler;

    private final DeviceContext context;

    private final DeviceContextService contextService;

    private final Executor executor;

    private final Queue<OperationRepresentation> queue = newConcurrentLinkedQueue();

    private volatile OperationRepresentation current;

    private final DeviceControlRepository deviceControl;

    private final Object lock = new Object();

    public SynchronizedOperationDispatcher(OperationExecutor delegate, Executor executor, DeviceContext context,
            DeviceContextService contextService, DeviceControlRepository deviceControl) {
        this.handler = delegate;
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
    public void dispatch(OperationRepresentation operation) {
        log.debug("opeartion dispatched {}", operation);
        queue.add(operation);
        executeNext();
    }

    private void executeNext() {
        if (isCurrentOperationFinished()) {
            log.warn("Operation was finished by external action interrupting execution {}", current);
            current = null;
        }
        executor.execute(contextService.withinContext(context, new PollQueue()));
    }

    private boolean isCurrentOperationFinished() {
        synchronized (lock) {
            if (isOperationInProgress()) {
                final OperationStatus status = getCurrentStatus(current);
                return FAILED.equals(status) || SUCCESSFUL.equals(status);
            } else {
                return false;
            }
        }

    }

    private boolean isOperationInProgress() {
        return current != null;
    }

    private final class PollQueue implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                if (!isOperationInProgress()) {
                    OperationRepresentation operation;
                    while ((operation = queue.poll()) != null) {
                        if (isExecutable(operation)) {
                            log.info("executing operation {}", operation);
                            current = operation;
                            try {
                                handler.handle(operation);
                            } catch (Exception ex) {
                                final Throwable rootCause = getRootCause(ex);

                                log.error("handle operation failed {}", operation, ex);
                                deviceControl
                                        .save(asFailedOperation(
                                                operation.getId(),
                                                format("Opertion dispatch failed %s : %s", rootCause.getClass().getName(),
                                                        rootCause.getMessage())));
                            }
                            executeNext();
                            break;
                        } else {
                            log.info("operation finished by external skiping execution {} ", operation);
                        }
                    }
                }
            }
        }

        private boolean isExecutable(OperationRepresentation operation) {
            return PENDING.equals(getCurrentStatus(operation));
        }

    }

    private OperationStatus getCurrentStatus(OperationRepresentation operation) {
        OperationRepresentation currentState = deviceControl.findById(operation.getId());
        return OperationStatus.valueOf(currentState.getStatus());
    }

}
