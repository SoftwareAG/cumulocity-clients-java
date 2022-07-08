package com.cumulocity.rest.representation.operation;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.DateTimeConverter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryLogEntryRepresentation extends AbstractExtensibleRepresentation {
    private String status;

    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime time;

}
