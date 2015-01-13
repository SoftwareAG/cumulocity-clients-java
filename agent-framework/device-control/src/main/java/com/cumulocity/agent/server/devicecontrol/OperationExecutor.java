package com.cumulocity.agent.server.devicecontrol;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

public interface OperationExecutor {
    boolean supports(OperationRepresentation operation);

    void handle(OperationRepresentation operation);

}
