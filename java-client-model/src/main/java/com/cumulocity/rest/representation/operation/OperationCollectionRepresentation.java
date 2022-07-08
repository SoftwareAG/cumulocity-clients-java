package com.cumulocity.rest.representation.operation;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class OperationCollectionRepresentation extends BaseCollectionRepresentation<OperationRepresentation> {
    
    private List<OperationRepresentation> operations;    

    @JSONTypeHint(OperationRepresentation.class)
    public List<OperationRepresentation> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationRepresentation> operations) {
        this.operations = operations;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<OperationRepresentation> iterator() {
        return operations.iterator();
    }
}
