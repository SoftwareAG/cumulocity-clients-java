package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeEventMessage extends AbstractExtensibleRepresentation {
    EventRepresentation data;
}
