package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeOperationMessage extends AbstractExtensibleRepresentation {
    OperationRepresentation data;
}
