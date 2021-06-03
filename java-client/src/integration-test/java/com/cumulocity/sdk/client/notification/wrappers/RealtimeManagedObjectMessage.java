package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeManagedObjectMessage extends AbstractExtensibleRepresentation {
    ManagedObjectRepresentation data;
}
