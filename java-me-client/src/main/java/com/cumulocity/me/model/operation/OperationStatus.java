package com.cumulocity.me.model.operation;


public class OperationStatus {

    public static final OperationStatus PENDING = new OperationStatus("PENDING");

    public static final OperationStatus SUCCESSFUL = new OperationStatus("SUCCESSFUL");

    public static final OperationStatus FAILED = new OperationStatus("FAILED");

    public static final OperationStatus EXECUTING = new OperationStatus("EXECUTING");

    private String name;

    private OperationStatus(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static OperationStatus valueOf(String status) {
        if (status.equals(PENDING.name)) {
            return PENDING;
        }
        else if (status.equals(SUCCESSFUL.name)) {
            return SUCCESSFUL;
        }
        else if (status.equals(FAILED.name)) {
            return FAILED;
        }
        else if (status.equals(EXECUTING.name)) {
            return EXECUTING;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    public String toString() {
        return name;
    }

}
