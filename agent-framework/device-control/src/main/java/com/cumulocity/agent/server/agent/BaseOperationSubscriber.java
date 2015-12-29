package com.cumulocity.agent.server.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.operation.Operations;

public abstract class BaseOperationSubscriber implements OperationSubscriber {

    private final static Logger logger = LoggerFactory.getLogger(BaseOperationSubscriber.class);
    
    protected DeviceControlRepository deviceControl;
    
    public BaseOperationSubscriber(DeviceControlRepository deviceControl) {
        this.deviceControl = deviceControl;
    }
    
    @Override
    public void executeOperation(OperationRepresentation operation) {
        logger.info("Start handling operation with id {}", operation.getId());
        setOperationExecuting(operation);
        handleOperation(operation);
    }
    
    public abstract void handleOperation(OperationRepresentation operation);
    
    public void setOperationExecuting(OperationRepresentation operation) {
        logger.info("Setting operation with id {} to EXECUTING", operation.getDeviceId());
        deviceControl.save(Operations.asExecutingOperation(operation.getId()));
    }
    
    public void setOperationFailed(OperationRepresentation operation, String failureReason) {
        logger.info("Setting operation with id {} to FAILED. Reason: {}", operation.getDeviceId(), failureReason);
        deviceControl.save(Operations.asFailedOperation(operation.getId(), failureReason));
    }
    
    public void setOperationSuccessful(OperationRepresentation operation) {
        logger.info("Setting operation with id {} to SUCCESSFUL", operation.getDeviceId());
        deviceControl.save(Operations.asSuccessOperation(operation.getId()));
    }

}
