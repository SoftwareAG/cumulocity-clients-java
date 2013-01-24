package com.cumulocity.me.rest.representation.devicecontrol;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

public class SimpleOperationProcessor /*implements OperationProcessor*/ {
    private List operations = new ArrayList();

    public boolean process(OperationRepresentation operation) {
        System.out.println("processing operationRep:" + operation.getId().toString() + " status:" + operation.getStatus());
        operations.add(operation);
        return true;
    }

    public List getOperations() {
        return operations;
    }

    public void setOperations(List operations) {
        this.operations = operations;
    }

}
