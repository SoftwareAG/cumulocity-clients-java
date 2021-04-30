package com.cumulocity.sdk.client.notification.wrappers;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealtimeMeasurementMessage extends AbstractExtensibleRepresentation {
    MeasurementRepresentation data;
}
