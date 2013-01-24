package com.cumulocity.sdk.client.devicecontrol;

import java.util.ArrayList;
import java.util.List;

import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationProcessor;

public class SimpleOperationProcessor implements OperationProcessor {
    private List<OperationRepresentation> operations = new ArrayList<OperationRepresentation>();

    @Override
    public boolean process(OperationRepresentation operation) {
        System.out.println("processing operationRep:" + operation.getId().toString() + " status:" + operation.getStatus());
        operations.add(operation);
        return true;
    }

    public List<OperationRepresentation> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationRepresentation> operations) {
        this.operations = operations;
    }

}
