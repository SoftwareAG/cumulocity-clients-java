package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeAlarmMessage extends AbstractExtensibleRepresentation {
    AlarmRepresentation data;
}
