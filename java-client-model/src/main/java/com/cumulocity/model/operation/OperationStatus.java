package com.cumulocity.model.operation;

public enum OperationStatus {

    PENDING, SUCCESSFUL, FAILED, EXECUTING;

    public static OperationStatus asOperationStatus(Object status) {
        if (status instanceof OperationStatus) {
            return (OperationStatus) status;
        } else {
            return asOperationStatus(String.valueOf(status));
        }
    }

    public static OperationStatus asOperationStatus(String status) {
        return status == null ? null : valueOf(status);
    }

}
