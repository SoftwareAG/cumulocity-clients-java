package com.cumulocity.rest.representation.operation;

import java.util.List;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Data;
import lombok.Getter;
import org.joda.time.DateTime;
import org.svenson.JSONTypeHint;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.DateTimeConverter;

@Data
public class DeliveryRepresentation extends AbstractExtensibleRepresentation {

    private String status;

    @Getter(onMethod_ = @JSONConverter(type = DateTimeConverter.class))
    private DateTime time;

    @Getter(onMethod_ = @JSONTypeHint(DeliveryLogEntryRepresentation.class))
    private List<DeliveryLogEntryRepresentation> log;
}
