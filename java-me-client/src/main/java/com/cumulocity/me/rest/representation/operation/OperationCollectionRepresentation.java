package com.cumulocity.me.rest.representation.operation;

import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class OperationCollectionRepresentation extends BaseCollectionRepresentation {
    
    private List operations;    

    public List getOperations() {
        return operations;
    }

    public void setOperations(List operations) {
        this.operations = operations;
    }
}
