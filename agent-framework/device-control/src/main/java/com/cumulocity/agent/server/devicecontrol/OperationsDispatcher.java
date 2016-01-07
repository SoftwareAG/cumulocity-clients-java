package com.cumulocity.agent.server.devicecontrol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

@Component
public class OperationsDispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(OperationsDispatcher.class);

    private final OperationDispacherRegistry dispatchersRegistry;

    @Autowired
    public OperationsDispatcher(OperationDispacherRegistry queueRegistry) {
        this.dispatchersRegistry = queueRegistry;
    }

    public void dispatch(OperationRepresentation operation) {
        LOG.debug("Processing device control requests deviceId: {} operation : {}", operation.getDeviceId(), operation.getId());
        for (OperationDispatcher handler : dispatchersRegistry.getDispatchers(operation.getDeviceId())) {
            if (handler.supports(operation)) {
                handler.dispatch(operation);
                return;
            }
        }
        LOG.warn("operation unhandled because matching operation hanlder not found :  {}", operation);
    }

}
