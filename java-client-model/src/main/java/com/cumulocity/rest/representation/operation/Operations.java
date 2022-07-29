package com.cumulocity.rest.representation.operation;

import static com.cumulocity.model.operation.OperationStatus.*;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.operations.NewMeasurement;

public class Operations {

    /**
     *  Returns operation that triggers a new measurement to be made.<p>
     *  Does not define the target device ID. Clients can interpret this as meaning measurements from all
     *  devices should be read, or they can use this as a base instance from which they can add device IDs.
     * @return a new measurement operation representation
     */
    public static OperationRepresentation createNewMeasurementOperation() {
        OperationRepresentation newOp = new OperationRepresentation();
        newOp.set(new NewMeasurement());
        return newOp;
    }

    public static OperationRepresentation asOperation(GId id) {
        final OperationRepresentation operation = new OperationRepresentation();
        operation.setId(id);
        return operation;
    }

    public static OperationRepresentation asExecutingOperation(GId id) {
        return asOperationWithStatus(id, EXECUTING);
    }

    public static OperationRepresentation asSuccessOperation(GId id) {
        return asOperationWithStatus(id, SUCCESSFUL);
    }

    public static OperationRepresentation asOperationWithStatus(GId id, OperationStatus status) {
        final OperationRepresentation operation = new OperationRepresentation();
        operation.setId(id);
        operation.setStatus(status.name());
        return operation;
    }

    public static OperationRepresentation asFailedOperation(GId id, String failureCause) {
        final OperationRepresentation operation = new OperationRepresentation();
        operation.setId(id);
        operation.setStatus(FAILED.name());
        operation.setFailureReason(failureCause);
        return operation;
    }
}
