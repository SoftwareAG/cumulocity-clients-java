package com.cumulocity.agent.server.devicecontrol;

import com.cumulocity.rest.representation.operation.OperationRepresentation;

public interface OperationDispatcher {

    boolean supports(OperationRepresentation operation);

    void dispatch(OperationRepresentation operation);

}
