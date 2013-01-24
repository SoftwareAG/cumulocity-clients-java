package com.cumulocity.me.rest.representation.operation;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class DeviceControlRepresentation extends BaseCumulocityResourceRepresentation {

    private OperationCollectionRepresentation operations;

    private String operationsByStatus;

    private String operationsByAgentId;
    
    private String operationsByAgentIdAndStatus;
    
    private String operationsByDeviceId;
    
    private String operationsByDeviceIdAndStatus;

    public OperationCollectionRepresentation getOperations() {
        return operations;
    }

    public void setOperations(OperationCollectionRepresentation operations) {
        this.operations = operations;
    }
    
    public String getOperationsByStatus() {
        return operationsByStatus;
    }

    public void setOperationsByStatus(String operationsByStatus) {
        this.operationsByStatus = operationsByStatus;
    }

    public String getOperationsByAgentId() {
        return operationsByAgentId;
    }

    public void setOperationsByAgentId(String operationsByAgentId) {
        this.operationsByAgentId = operationsByAgentId;
    }

    public String getOperationsByAgentIdAndStatus() {
        return operationsByAgentIdAndStatus;
    }

    public void setOperationsByAgentIdAndStatus(String operationsByAgentIdAndStatus) {
        this.operationsByAgentIdAndStatus = operationsByAgentIdAndStatus;
    }
    
    public String getOperationsByDeviceId() {
        return operationsByDeviceId;
    }

    public void setOperationsByDeviceId(String operationsByDeviceId) {
        this.operationsByDeviceId = operationsByDeviceId;
    }
    
    public String getOperationsByDeviceIdAndStatus() {
        return operationsByDeviceIdAndStatus;
    }

    public void setOperationsByDeviceIdAndStatus(String operationsByDeviceIdAndStatus) {
        this.operationsByDeviceIdAndStatus = operationsByDeviceIdAndStatus;
    }
}
