package com.cumulocity.agent.server.agent;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

public interface OperationSubscriber {

    void executeOperation(OperationRepresentation operation);
}
