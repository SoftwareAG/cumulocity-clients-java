package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeDeleteMessage extends AbstractExtensibleRepresentation {
    String data;
}
